package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetPendingApprovalsResponseDto;
import com.tuyenngoc.army2forum.domain.entity.ClanApproval;
import com.tuyenngoc.army2forum.repository.ClanApprovalRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.ClanApprovalService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClanApprovalServiceImpl implements ClanApprovalService {

    private final ClanApprovalRepository clanApprovalRepository;

    @Override
    public PaginationResponseDto<GetPendingApprovalsResponseDto> getPendingApprovals(Long clanId, CustomUserDetails userDetails, PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.CLAN_MEMBER);

        Page<ClanApproval> page = clanApprovalRepository.findAll(pageable);

        List<GetPendingApprovalsResponseDto> items = page.getContent().stream()
                .map(GetPendingApprovalsResponseDto::new)
                .collect(Collectors.toList());

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.CLAN_MEMBER, page);

        PaginationResponseDto<GetPendingApprovalsResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public CommonResponseDto approveMember(Long clanId, Long approvalId, Long playerId) {
        return null;
    }

    @Override
    public CommonResponseDto rejectMember(Long clanId, Long approvalId, Long playerId) {
        return null;
    }

}
