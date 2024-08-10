package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.PlayerNotificationRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.PlayerNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Player Notification")
public class PlayerNotificationController {

    PlayerNotificationService playerNotificationService;

    @Operation(summary = "API get player notifications")
    @GetMapping(UrlConstant.PlayerNotification.GET_ALL)
    public ResponseEntity<?> getPlayerNotifications(
            @ParameterObject PaginationRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerNotificationService.getPlayerNotifications(userDetails.getPlayerId(), requestDto));
    }

    @Operation(summary = "API get player notification by id")
    @GetMapping(UrlConstant.PlayerNotification.GET_BY_ID)
    public ResponseEntity<?> getPlayerNotificationById(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerNotificationService.getPlayerNotificationById(id, userDetails.getPlayerId()));
    }

    @Operation(summary = "API delete player notification by id")
    @DeleteMapping(UrlConstant.PlayerNotification.DELETE)
    public ResponseEntity<?> deletePlayerNotificationById(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(playerNotificationService.deletePlayerNotificationById(id, userDetails.getPlayerId()));
    }

    @Operation(summary = "API create new player notification")
    @PostMapping(UrlConstant.PlayerNotification.CREATE)
    public ResponseEntity<?> createPlayerNotification(@Valid @RequestBody PlayerNotificationRequestDto playerNotificationRequestDto) {
        return VsResponseUtil.success(playerNotificationService.createPlayerNotification(playerNotificationRequestDto));
    }

}