package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationSortRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePointsRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Toggle Equipment Chest Lock")
    @PutMapping(UrlConstant.Player.TOGGLE_CHEST_LOCK)
    public ResponseEntity<?> toggleEquipmentChestLock(
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerService.toggleEquipmentChestLock(userDetails.getPlayerId()));
    }

    @Operation(summary = "Toggle Invitation Lock")
    @PutMapping(UrlConstant.Player.TOGGLE_INVITATION_LOCK)
    public ResponseEntity<?> toggleInvitationLock(
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerService.toggleInvitationLock(userDetails.getPlayerId()));
    }

    @Operation(summary = "Get player inventory items")
    @GetMapping(UrlConstant.Player.GET_PLAYER_INVENTORY)
    public ResponseEntity<?> getPlayerInventory(
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerService.getPlayerInventory(userDetails.getPlayerId()));
    }

    @Operation(summary = "Update additional points")
    @PutMapping(UrlConstant.Player.UPDATE_ADDITIONAL_POINTS)
    public ResponseEntity<?> updateAdditionalPoints(
            @Valid @RequestBody UpdatePointsRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerService.updateAdditionalPoints(requestDto, userDetails));
    }

    @Operation(summary = "Get player character")
    @GetMapping(UrlConstant.Player.GET_PLAYER_CHARACTER)
    public ResponseEntity<?> getPlayerCharacter(
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerService.getPlayerCharacter(userDetails.getPlayerId()));
    }

    @Operation(summary = "Get player points")
    @GetMapping(UrlConstant.Player.GET_PLAYER_POINTS)
    public ResponseEntity<?> getPlayerPoints(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerService.getPlayerPoints(userDetails.getPlayerId(), id));
    }
}
