package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetPendingApprovalsResponseDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

public interface ClanApprovalService {

    PaginationResponseDto<GetPendingApprovalsResponseDto> getPendingApprovals(Long clanId, CustomUserDetails userDetails, PaginationFullRequestDto requestDto);

    CommonResponseDto approveMember(Long clanId, Long approvalId, Long playerId);

    CommonResponseDto rejectMember(Long clanId, Long approvalId, Long playerId);

}
