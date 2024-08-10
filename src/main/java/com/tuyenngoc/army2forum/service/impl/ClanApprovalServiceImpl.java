package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ClanMemberRightsConstants;
import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetPendingApprovalsResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Clan;
import com.tuyenngoc.army2forum.domain.entity.ClanApproval;
import com.tuyenngoc.army2forum.domain.entity.ClanMember;
import com.tuyenngoc.army2forum.exception.ConflictException;
import com.tuyenngoc.army2forum.exception.ForbiddenException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.ClanApprovalRepository;
import com.tuyenngoc.army2forum.repository.ClanMemberRepository;
import com.tuyenngoc.army2forum.repository.ClanRepository;
import com.tuyenngoc.army2forum.service.ClanApprovalService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClanApprovalServiceImpl implements ClanApprovalService {

    ClanMemberRepository clanMemberRepository;

    ClanRepository clanRepository;

    ClanApprovalRepository clanApprovalRepository;

    MessageSource messageSource;

    @Override
    public PaginationResponseDto<GetPendingApprovalsResponseDto> getPendingApprovals(Long clanId, Long playerId, PaginationRequestDto requestDto) {
        Optional<ClanMember> clanMember = clanMemberRepository.findByClanIdAndPlayerId(clanId, playerId);
        if (clanMember.isEmpty() || Objects.equals(clanMember.get().getRights(), ClanMemberRightsConstants.CLAN_MEMBER)) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN);
        }

        Pageable pageable = PaginationUtil.buildPageable(requestDto);

        Page<ClanApproval> page = clanApprovalRepository.findAll(pageable);

        List<GetPendingApprovalsResponseDto> items = page.getContent().stream()
                .map(GetPendingApprovalsResponseDto::new)
                .collect(Collectors.toList());

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, page);

        PaginationResponseDto<GetPendingApprovalsResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public CommonResponseDto approveMember(Long clanId, Long approvalId, Long playerId) {
        Optional<ClanMember> memberOptional = clanMemberRepository.findByClanIdAndPlayerId(clanId, playerId);
        if (memberOptional.isEmpty() || Objects.equals(memberOptional.get().getRights(), ClanMemberRightsConstants.CLAN_MEMBER)) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN);
        }

        ClanApproval clanApproval = clanApprovalRepository.findByIdAndClanId(approvalId, clanId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ClanApproval.ERR_NOT_FOUND_ID, approvalId));

        if (clanApproval.getPlayer().getClanMember() != null) {
            throw new ConflictException(ErrorMessage.ClanApproval.ERR_ALREADY_MEMBER);
        }

        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_FOUND_ID, clanId));

        ClanMember clanMember = new ClanMember();
        clanMember.setClan(clan);
        clanMember.setPlayer(clanApproval.getPlayer());
        clanMember.setRights(ClanMemberRightsConstants.CLAN_MEMBER);
        clanMember.setJoinTime(LocalDateTime.now());

        clanMemberRepository.save(clanMember);
        clanApprovalRepository.delete(clanApproval);

        String message = messageSource.getMessage(SuccessMessage.ClanApproval.APPROVAL_SUCCESS, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto rejectMember(Long clanId, Long approvalId, Long playerId) {
        Optional<ClanMember> memberOptional = clanMemberRepository.findByClanIdAndPlayerId(clanId, playerId);
        if (memberOptional.isEmpty() || Objects.equals(memberOptional.get().getRights(), ClanMemberRightsConstants.CLAN_MEMBER)) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN);
        }

        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_FOUND_ID, clanId));

        if (!Objects.equals(clan.getMaster().getId(), playerId)) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN);
        }

        ClanApproval clanApproval = clanApprovalRepository.findByIdAndClanId(approvalId, clanId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ClanApproval.ERR_NOT_FOUND_ID, approvalId));

        clanApprovalRepository.delete(clanApproval);

        String message = messageSource.getMessage(SuccessMessage.ClanApproval.REJECTION_SUCCESS, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

}
