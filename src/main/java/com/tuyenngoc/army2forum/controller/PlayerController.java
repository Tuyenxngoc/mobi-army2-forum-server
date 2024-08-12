package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationSortRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePointsRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Player")
public class PlayerController {

    PlayerService playerService;

    @Operation(summary = "API get following posts")
    @GetMapping(UrlConstant.Player.GET_FOLLOWING)
    public ResponseEntity<?> getFollowingPosts(
            @ParameterObject PaginationSortRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerService.getFollowingPosts(userDetails.getPlayerId(), requestDto));
    }

    @Operation(summary = "Get player by ID")
    @GetMapping(UrlConstant.Player.GET_BY_ID)
    public ResponseEntity<?> getPlayerInfoById(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerService.getPlayerInfoById(id, userDetails.getPlayerId()));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API get players")
    @GetMapping(UrlConstant.Player.GET_ALL)
    public ResponseEntity<?> getPlayers(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(playerService.getPlayers(requestDto));
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
