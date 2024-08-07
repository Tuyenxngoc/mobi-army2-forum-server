package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.ClanMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestApiV1
@RequiredArgsConstructor
@Tag(name = "Clan Member")
public class ClanMemberController {

    private final ClanMemberService clanMemberService;

    @Operation(summary = "API Join Clan")
    @PostMapping(UrlConstant.ClanMember.JOIN)
    public ResponseEntity<?> joinClan(
            @PathVariable Long clanId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(clanMemberService.joinClan(clanId, userDetails));
    }

    @Operation(summary = "API Leave Clan")
    @PostMapping(UrlConstant.ClanMember.LEAVE)
    public ResponseEntity<?> leaveClan(
            @PathVariable Long clanId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(clanMemberService.leaveClan(clanId, userDetails));
    }

    @Operation(summary = "API Get Clan Members")
    @GetMapping(UrlConstant.ClanMember.GET_MEMBERS)
    public ResponseEntity<?> getClanMembers(
            @PathVariable Long clanId,
            @ParameterObject PaginationFullRequestDto requestDto
    ) {
        return VsResponseUtil.success(clanMemberService.getClanMembers(clanId, requestDto));
    }

    @Operation(summary = "API Get Clan Members For Owner")
    @GetMapping(UrlConstant.ClanMember.ADMIN_GET_MEMBERS)
    public ResponseEntity<?> getClanMembersForOwner(
            @PathVariable Long clanId,
            @CurrentUser CustomUserDetails userDetails,
            @ParameterObject PaginationFullRequestDto requestDto
    ) {
        return VsResponseUtil.success(clanMemberService.getClanMembersForOwner(clanId, userDetails.getPlayerId(), requestDto));
    }

    @Operation(summary = "API Remove Member")
    @DeleteMapping(UrlConstant.ClanMember.REMOVE_MEMBER)
    public ResponseEntity<?> removeMember(
            @PathVariable Long clanId,
            @PathVariable Long memberId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(clanMemberService.removeMember(clanId, memberId, userDetails.getPlayerId()));
    }

    @Operation(summary = "API Promote Member")
    @PostMapping(UrlConstant.ClanMember.PROMOTE_MEMBER)
    public ResponseEntity<?> promoteMember(
            @PathVariable Long clanId,
            @PathVariable Long memberId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(clanMemberService.promoteMember(clanId, memberId, userDetails.getPlayerId()));
    }

}
