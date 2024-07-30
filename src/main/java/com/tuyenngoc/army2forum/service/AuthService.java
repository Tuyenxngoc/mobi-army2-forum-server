package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.request.auth.*;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.auth.LoginResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.auth.TokenRefreshResponseDto;
import com.tuyenngoc.army2forum.domain.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto request);

    CommonResponseDto logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

    TokenRefreshResponseDto refresh(TokenRefreshRequestDto request);

    User register(RegisterRequestDto requestDto, String siteURL);

    CommonResponseDto forgetPassword(ForgetPasswordRequestDto requestDto);

    CommonResponseDto changePassword(ChangePasswordRequestDto requestDto, String username);

    CommonResponseDto confirmEmail(String code);

    CommonResponseDto resendConfirmationEmail(String email, String siteURL);

    boolean isEmailConfirmed(String email);

    LoginResponseDto handleOAuth2Callback(String code, HttpServletRequest request, HttpServletResponse response);

}
