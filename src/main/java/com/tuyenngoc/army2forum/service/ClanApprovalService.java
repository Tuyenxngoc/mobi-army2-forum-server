package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetPendingApprovalsResponseDto;

public interface ClanApprovalService {

    PaginationResponseDto<GetPendingApprovalsResponseDto> getPendingApprovals(Long clanId, Long playerId, PaginationRequestDto requestDto);

    CommonResponseDto approveMember(Long clanId, Long approvalId, Long playerId);

    CommonResponseDto rejectMember(Long clanId, Long approvalId, Long playerId);

}
