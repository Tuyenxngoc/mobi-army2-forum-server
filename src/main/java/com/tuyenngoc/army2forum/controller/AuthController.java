package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.request.auth.*;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Auth")
public class AuthController {

    AuthService authService;

    @Operation(summary = "API Login")
    @PostMapping(UrlConstant.Auth.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto request) {
        return VsResponseUtil.success(authService.login(request));
    }

    @Operation(summary = "API Logout")
    @PostMapping(UrlConstant.Auth.LOGOUT)
    public ResponseEntity<?> logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        return VsResponseUtil.success(authService.logout(request, response, authentication));
    }

    @Operation(summary = "API Refresh token")
    @PostMapping(UrlConstant.Auth.REFRESH_TOKEN)
    public ResponseEntity<?> refresh(@Valid @RequestBody TokenRefreshRequestDto tokenRefreshRequestDto) {
        return VsResponseUtil.success(authService.refresh(tokenRefreshRequestDto));
    }

    @Operation(summary = "API Register")
    @PostMapping(UrlConstant.Auth.REGISTER)
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequestDto requestDto,
            @RequestParam("siteURL") String siteURL
    ) {
        return VsResponseUtil.success(authService.register(requestDto, siteURL));
    }

    @Operation(summary = "API Confirm registration")
    @GetMapping(UrlConstant.Auth.CONFIRM)
    public ResponseEntity<?> confirmEmail(@RequestParam("code") String code) {
        return VsResponseUtil.success(authService.confirmEmail(code));
    }

    @Operation(summary = "API resend confirmation email")
    @PostMapping(UrlConstant.Auth.RESEND_CONFIRMATION_EMAIL)
    public ResponseEntity<?> resendConfirmationEmail(
            @RequestParam("email") String email,
            @RequestParam("siteURL") String siteURL
    ) {
        return VsResponseUtil.success(authService.resendConfirmationEmail(email, siteURL));
    }

    @Operation(summary = "API forget password")
    @PostMapping(UrlConstant.Auth.FORGET_PASSWORD)
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody ForgetPasswordRequestDto requestDto) {
        return VsResponseUtil.success(authService.forgetPassword(requestDto));
    }

    @Operation(summary = "API change password")
    @PatchMapping(UrlConstant.Auth.CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(authService.changePassword(requestDto, userDetails.getUsername()));
    }

    @Operation(summary = "API check if email is confirmed")
    @GetMapping(UrlConstant.Auth.CHECK_EMAIL_CONFIRMED)
    public ResponseEntity<?> checkEmailConfirmed(@RequestParam("email") String email) {
        return VsResponseUtil.success(authService.isEmailConfirmed(email));
    }

    @Operation(summary = "API Login with Google")
    @PostMapping(UrlConstant.Auth.GOOGLE_LOGIN)
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, Object> payload) {
        return VsResponseUtil.success(authService.loginWithGoogle(payload));
    }

}
