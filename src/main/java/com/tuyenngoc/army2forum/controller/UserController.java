package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateUserRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @Operation(summary = "API get current user login")
    @GetMapping(UrlConstant.User.GET_CURRENT_USER)
    public ResponseEntity<?> getCurrentUser(
            @Parameter(name = "userDetails", hidden = true)
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(userService.getCurrentUser(userDetails.getUsername()));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Update User")
    @PutMapping(UrlConstant.User.UPDATE)
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequestDto requestDto
    ) {
        return VsResponseUtil.success(userService.updateUser(id, requestDto));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Delete User")
    @DeleteMapping(UrlConstant.User.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        return VsResponseUtil.success(userService.deleteUser(id));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Get User by Id")
    @GetMapping(UrlConstant.User.GET_BY_ID)
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        return VsResponseUtil.success(userService.getUserById(id));
    }

    @Operation(summary = "API Get Users")
    @GetMapping(UrlConstant.User.GET_ALL)
    public ResponseEntity<?> getAllUsers(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(userService.getUsers(requestDto));
    }
}
