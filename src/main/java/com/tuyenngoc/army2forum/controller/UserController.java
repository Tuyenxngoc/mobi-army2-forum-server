package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.request.ChangeUsernameRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.LockUserRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User")
public class UserController {

    UserService userService;

    @Operation(summary = "API get current user login")
    @GetMapping(UrlConstant.User.GET_CURRENT_USER)
    public ResponseEntity<?> getCurrentUser(@CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(userService.getCurrentUser(userDetails.getPlayerId()));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @Operation(summary = "Update user roles")
    @PostMapping(UrlConstant.User.UPDATE_ROLE)
    public ResponseEntity<?> updatePlayerRoles(
            @PathVariable Long playerId,
            @PathVariable Byte roleId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(userService.updateUserRoles(playerId, roleId, userDetails));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Lock User Account")
    @PutMapping(UrlConstant.User.LOCK)
    public ResponseEntity<?> lockUserAccount(
            @PathVariable Long playerId,
            @Valid @RequestBody LockUserRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(userService.lockUserAccount(playerId, requestDto, userDetails));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Get User by Id")
    @GetMapping(UrlConstant.User.GET_BY_ID)
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        return VsResponseUtil.success(userService.getUserById(id));
    }

    @Operation(summary = "API Change Username")
    @PutMapping(UrlConstant.User.CHANGE_USERNAME)
    public ResponseEntity<?> changeUsername(
            @Valid @RequestBody ChangeUsernameRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(userService.changeUsername(userDetails, requestDto));
    }

}
