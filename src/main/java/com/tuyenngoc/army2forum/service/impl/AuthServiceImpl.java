package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.RoleConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.UserInfo;
import com.tuyenngoc.army2forum.domain.dto.common.DataMailDto;
import com.tuyenngoc.army2forum.domain.dto.request.auth.*;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.auth.LoginResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.auth.TokenRefreshResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.User;
import com.tuyenngoc.army2forum.domain.mapper.UserMapper;
import com.tuyenngoc.army2forum.exception.BadRequestException;
import com.tuyenngoc.army2forum.exception.ConflictException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.exception.UnauthorizedException;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.UserRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.security.jwt.JwtTokenProvider;
import com.tuyenngoc.army2forum.service.*;
import com.tuyenngoc.army2forum.util.RandomPasswordUtil;
import com.tuyenngoc.army2forum.util.SendMailUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    AuthenticationManager authenticationManager;

    JwtTokenProvider jwtTokenProvider;

    JwtTokenService jwtTokenService;

    EmailRateLimiterService emailRateLimiterService;

    MessageSource messageSource;

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    RoleService roleService;

    SendMailUtil sendMailUtil;

    PlayerRepository playerRepository;

    PlayerCharactersService playerCharactersService;

    OAuth2GoogleService oAuth2GoogleService;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException(ErrorMessage.Auth.ERR_INCORRECT_USERNAME_PASSWORD));

        checkUserLocked(user);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtTokenProvider.generateToken(customUserDetails, Boolean.FALSE);
            String refreshToken = jwtTokenProvider.generateToken(customUserDetails, Boolean.TRUE);

            jwtTokenService.saveAccessToken(accessToken, customUserDetails.getUserId());
            jwtTokenService.saveRefreshToken(refreshToken, customUserDetails.getUserId());

            return new LoginResponseDto(
                    accessToken,
                    refreshToken,
                    customUserDetails.getAuthorities()
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(ErrorMessage.Auth.ERR_INCORRECT_USERNAME_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessage.ERR_EXCEPTION_GENERAL);
        }
    }

    @Override
    public CommonResponseDto logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        if (authentication != null && authentication.getPrincipal() != null) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            jwtTokenService.deleteTokens(customUserDetails.getUserId());
        }

        SecurityContextLogoutHandler logout = new SecurityContextLogoutHandler();
        logout.logout(request, response, authentication);

        String message = messageSource.getMessage(SuccessMessage.Auth.LOGOUT, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public TokenRefreshResponseDto refresh(TokenRefreshRequestDto request) {
        String refreshToken = request.getRefreshToken();

        if (jwtTokenProvider.validateToken(refreshToken)) {
            String userId = jwtTokenProvider.extractSubjectFromJwt(refreshToken);

            if (jwtTokenService.isRefreshTokenExists(refreshToken, userId)) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new BadRequestException(ErrorMessage.Auth.ERR_INVALID_REFRESH_TOKEN));
                CustomUserDetails userDetails = CustomUserDetails.create(user);

                String newAccessToken = jwtTokenProvider.generateToken(userDetails, Boolean.FALSE);
                String newRefreshToken = jwtTokenProvider.generateToken(userDetails, Boolean.TRUE);

                jwtTokenService.saveAccessToken(newAccessToken, user.getId());
                jwtTokenService.saveRefreshToken(newRefreshToken, user.getId());

                return new TokenRefreshResponseDto(newAccessToken, newRefreshToken);
            } else {
                throw new BadRequestException(ErrorMessage.Auth.ERR_INVALID_REFRESH_TOKEN);
            }
        } else {
            throw new BadRequestException(ErrorMessage.Auth.ERR_INVALID_REFRESH_TOKEN);
        }
    }

    @Override
    public User register(RegisterRequestDto requestDto, String siteURL) {
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new BadRequestException(ErrorMessage.INVALID_REPEAT_PASSWORD);
        }
        boolean isUsernameExists = userRepository.existsByUsername(requestDto.getUsername());
        if (isUsernameExists) {
            throw new ConflictException(ErrorMessage.Auth.ERR_DUPLICATE_USERNAME);
        }
        boolean isEmailExists = userRepository.existsByEmail(requestDto.getEmail());
        if (isEmailExists) {
            throw new ConflictException(ErrorMessage.Auth.ERR_DUPLICATE_EMAIL);
        }

        String code = UUID.randomUUID().toString();

        //Create new User
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(roleService.getRole(RoleConstant.ROLE_USER.name()));
        user.setVerificationCode(code);
        user.setIsEnabled(false);
        user.setIsLocked(false);
        userRepository.save(user);

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", requestDto.getUsername());
        properties.put("password", requestDto.getPassword());
        properties.put("url", siteURL + "/verify?code=" + code);
        sendEmail(requestDto.getEmail(), "Đăng ký thành công", properties, "registerSuccess.html");

        return user;
    }

    @Override
    public CommonResponseDto forgetPassword(ForgetPasswordRequestDto requestDto) {
        User user = userRepository.findByUsernameAndEmail(requestDto.getUsername(), requestDto.getEmail())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ACCOUNT));

        // Kiểm tra giới hạn thời gian gửi email
        if (emailRateLimiterService.isMailLimited(requestDto.getEmail())) {
            String message = messageSource.getMessage(ErrorMessage.User.RATE_LIMIT, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(message);
        }

        String newPassword = RandomPasswordUtil.random();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", requestDto.getUsername());
        properties.put("newPassword", newPassword);
        sendEmail(user.getEmail(), "Lấy lại mật khẩu", properties, "forgetPassword.html");

        emailRateLimiterService.setMailLimit(requestDto.getEmail(), 1, TimeUnit.MINUTES);

        String message = messageSource.getMessage(SuccessMessage.User.FORGET_PASSWORD, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto changePassword(ChangePasswordRequestDto requestDto, String username) {
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new BadRequestException(ErrorMessage.INVALID_REPEAT_PASSWORD);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME, username));

        boolean isCorrectPassword = passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword());
        if (!isCorrectPassword) {
            throw new BadRequestException(ErrorMessage.Auth.ERR_INCORRECT_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);

        Map<String, Object> properties = new HashMap<>();
        properties.put("currentTime", new Date());
        sendEmail(user.getEmail(), "Đổi mật khẩu thành công", properties, "changePassword.html");

        String message = messageSource.getMessage(SuccessMessage.User.CHANGE_PASSWORD, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto confirmEmail(String code) {
        Optional<User> optionalUser = userRepository.findByVerificationCode(code);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getIsEnabled()) {
                String message = messageSource.getMessage(ErrorMessage.User.ALREADY_VERIFIED, null, LocaleContextHolder.getLocale());
                return new CommonResponseDto(message);
            }

            user.setVerificationCode(null);
            user.setIsEnabled(true);
            userRepository.save(user);

            //Create new Player
            Player player = new Player();
            player.setUser(user);
            playerRepository.save(player);

            playerCharactersService.initiatePlayerCharacterDefaults(player);

            String message = messageSource.getMessage(SuccessMessage.User.VERIFIED_SUCCESS, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(message);
        } else {
            String message = messageSource.getMessage(ErrorMessage.User.INVALID_VERIFICATION_CODE, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(message);
        }
    }

    @Override
    public CommonResponseDto resendConfirmationEmail(String email, String siteURL) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getIsEnabled()) {
                String message = messageSource.getMessage(ErrorMessage.User.ALREADY_VERIFIED, null, LocaleContextHolder.getLocale());
                return new CommonResponseDto(message);
            }

            // Kiểm tra giới hạn thời gian gửi email
            if (emailRateLimiterService.isMailLimited(email)) {
                String message = messageSource.getMessage(ErrorMessage.User.RATE_LIMIT, null, LocaleContextHolder.getLocale());
                return new CommonResponseDto(message);
            }

            String code = UUID.randomUUID().toString();
            user.setVerificationCode(code);
            userRepository.save(user);

            emailRateLimiterService.setMailLimit(email, 1, TimeUnit.MINUTES);

            Map<String, Object> properties = new HashMap<>();
            properties.put("url", siteURL + "/verify?code=" + code);
            sendEmail(user.getEmail(), "Kích hoạt tài khoản", properties, "resendConfirmationEmail.html");

            String message = messageSource.getMessage(SuccessMessage.User.RESEND_CONFIRMATION, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(message);
        } else {
            String message = messageSource.getMessage(ErrorMessage.User.INVALID_EMAIL, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(message);
        }
    }

    @Override
    public boolean isEmailConfirmed(String email) {
        return userRepository.existsByEmailAndIsEnabledTrue(email);
    }

    private void sendEmail(String to, String subject, Map<String, Object> properties, String templateName) {
        // Tạo DataMailDto
        DataMailDto mailDto = new DataMailDto();
        mailDto.setTo(to);
        mailDto.setSubject(subject);
        mailDto.setProperties(properties);

        // Gửi email bất đồng bộ
        CompletableFuture.runAsync(() -> {
            try {
                sendMailUtil.sendEmailWithHTML(mailDto, templateName);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public LoginResponseDto loginWithGoogle(Map<String, Object> payload) {
        String token = (String) payload.get("access_token");
        if (token == null || token.trim().isEmpty()) {
            throw new BadRequestException(ErrorMessage.Auth.INVALID_ACCESS_TOKEN);
        }

        UserInfo userInfo;
        try {
            userInfo = oAuth2GoogleService.verifyAccessToken(token);
        } catch (Exception e) {
            throw new BadRequestException(ErrorMessage.Auth.INVALID_ACCESS_TOKEN);
        }

        Optional<User> optionalUser = userRepository.findByEmail(userInfo.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            checkUserLocked(user);

            CustomUserDetails userDetails = CustomUserDetails.create(user);

            String accessToken = jwtTokenProvider.generateToken(userDetails, Boolean.FALSE);
            String refreshToken = jwtTokenProvider.generateToken(userDetails, Boolean.TRUE);

            jwtTokenService.saveAccessToken(accessToken, user.getId());
            jwtTokenService.saveRefreshToken(refreshToken, user.getId());

            return new LoginResponseDto(
                    accessToken,
                    refreshToken,
                    userDetails.getAuthorities()
            );
        } else {
            throw new UnauthorizedException(ErrorMessage.Auth.USER_NOT_FOUND);
        }
    }

    private void checkUserLocked(User user) {
        if (user.getIsLocked()) {
            LocalDate lockUntil = user.getLockUntil();

            // Nếu lockUntil là null, tài khoản bị khóa vĩnh viễn
            if (lockUntil == null) {
                throw new UnauthorizedException(ErrorMessage.Auth.ERR_ACCOUNT_LOCKED, null, null);
            }

            // Nếu thời gian hiện tại đã qua lockUntil, mở khóa tài khoản
            if (LocalDate.now().isAfter(lockUntil)) {
                user.setIsLocked(false);
                user.setLockUntil(null);
                user.setLockReason(null);

                userRepository.save(user);
            } else {
                // Nếu tài khoản vẫn bị khóa, ném ngoại lệ với lý do và thời gian khóa
                String lockReason = user.getLockReason();
                throw new UnauthorizedException(ErrorMessage.Auth.ERR_ACCOUNT_LOCKED, lockUntil, lockReason);
            }
        }
    }

}
