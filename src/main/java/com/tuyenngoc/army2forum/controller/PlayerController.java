package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationSortRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestApiV1
@RequiredArgsConstructor
@Tag(name = "Player")
public class PlayerController {

    private final PlayerService playerService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "Update player roles")
    @PostMapping(UrlConstant.Player.UPDATE_ROLE)
    public ResponseEntity<?> updatePlayerRoles(
            @PathVariable Long id,
            @PathVariable Byte roleId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerService.updatePlayerRoles(id, roleId, userDetails));
    }

    @Operation(summary = "API get following posts")
    @GetMapping(UrlConstant.Player.GET_FOLLOWING)
    public ResponseEntity<?> getFollowingPosts(
            @ParameterObject PaginationSortRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerService.getFollowingPosts(userDetails.getPlayerId(), requestDto));
    }

    @Operation(summary = "Get player information by ID")
    @GetMapping(UrlConstant.Player.GET_PLAYER_INFO)
    public ResponseEntity<?> getPlayerInfo(
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerService.getPlayerInfo(userDetails.getPlayerId()));
    }

}
