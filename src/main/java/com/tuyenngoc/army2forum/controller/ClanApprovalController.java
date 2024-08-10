package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.ClanApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Clan Approval")
public class ClanApprovalController {

    ClanApprovalService clanApprovalService;

    @Operation(summary = "API Get Pending Approvals")
    @GetMapping(UrlConstant.ClanApproval.GET_PENDING_APPROVALS)
    public ResponseEntity<?> getPendingApprovals(
            @PathVariable Long clanId,
            @CurrentUser CustomUserDetails userDetails,
            @ParameterObject PaginationRequestDto requestDto
    ) {
        return VsResponseUtil.success(clanApprovalService.getPendingApprovals(clanId, userDetails.getPlayerId(), requestDto));
    }

    @Operation(summary = "API Approve Member")
    @PostMapping(UrlConstant.ClanApproval.APPROVE_MEMBER)
    public ResponseEntity<?> approveMember(
            @PathVariable Long clanId,
            @PathVariable Long approvalId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(clanApprovalService.approveMember(clanId, approvalId, userDetails.getPlayerId()));
    }

    @Operation(summary = "API Reject Member")
    @PostMapping(UrlConstant.ClanApproval.REJECT_MEMBER)
    public ResponseEntity<?> rejectMember(
            @PathVariable Long clanId,
            @PathVariable Long approvalId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(clanApprovalService.rejectMember(clanId, approvalId, userDetails.getPlayerId()));
    }
}
