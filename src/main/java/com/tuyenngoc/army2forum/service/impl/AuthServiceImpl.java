package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.RoleConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.common.DataMailDto;
import com.tuyenngoc.army2forum.domain.dto.request.*;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.auth.LoginResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.auth.TokenRefreshResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.User;
import com.tuyenngoc.army2forum.domain.mapper.UserMapper;
import com.tuyenngoc.army2forum.exception.DataIntegrityViolationException;
import com.tuyenngoc.army2forum.exception.InvalidException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.exception.UnauthorizedException;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.UserRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.security.jwt.JwtTokenProvider;
import com.tuyenngoc.army2forum.service.AuthService;
import com.tuyenngoc.army2forum.service.EmailRateLimiterService;
import com.tuyenngoc.army2forum.service.JwtTokenService;
import com.tuyenngoc.army2forum.service.RoleService;
import com.tuyenngoc.army2forum.util.RandomPasswordUtil;
import com.tuyenngoc.army2forum.util.SendMailUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtTokenService jwtTokenService;

    private final EmailRateLimiterService emailRateLimiterService;

    private final MessageSource messageSource;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final SendMailUtil sendMailUtil;

    private final PlayerRepository playerRepository;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            String accessToken = jwtTokenProvider.generateToken(customUserDetails, Boolean.FALSE);
            String refreshToken = jwtTokenProvider.generateToken(customUserDetails, Boolean.TRUE);

            jwtTokenService.saveAccessToken(accessToken, customUserDetails.getUsername());
            jwtTokenService.saveRefreshToken(refreshToken, customUserDetails.getUsername());

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
        if (authentication != null && authentication.getName() != null) {
            jwtTokenService.deleteTokens(authentication.getName());
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
            String username = jwtTokenProvider.extractClaimUsername(refreshToken);

            if (jwtTokenService.isRefreshTokenExists(refreshToken, username)) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new InvalidException(ErrorMessage.Auth.ERR_INVALID_REFRESH_TOKEN));
                CustomUserDetails userDetails = CustomUserDetails.create(user);

                String newAccessToken = jwtTokenProvider.generateToken(userDetails, Boolean.FALSE);
                String newRefreshToken = jwtTokenProvider.generateToken(userDetails, Boolean.TRUE);

                jwtTokenService.saveAccessToken(newAccessToken, user.getUsername());
                jwtTokenService.saveRefreshToken(newRefreshToken, user.getUsername());

                return new TokenRefreshResponseDto(newAccessToken, newRefreshToken);
            } else {
                throw new InvalidException(ErrorMessage.Auth.ERR_INVALID_REFRESH_TOKEN);
            }
        } else {
            throw new InvalidException(ErrorMessage.Auth.ERR_INVALID_REFRESH_TOKEN);
        }
    }

    @Override
    public User register(RegisterRequestDto requestDto, String siteURL) {
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new InvalidException(ErrorMessage.INVALID_REPEAT_PASSWORD);
        }
        boolean isUsernameExists = userRepository.existsByUsername(requestDto.getUsername());
        if (isUsernameExists) {
            throw new DataIntegrityViolationException(ErrorMessage.Auth.ERR_DUPLICATE_USERNAME);
        }
        boolean isEmailExists = userRepository.existsByEmail(requestDto.getEmail());
        if (isEmailExists) {
            throw new DataIntegrityViolationException(ErrorMessage.Auth.ERR_DUPLICATE_EMAIL);
        }

        String code = UUID.randomUUID().toString();

        //Create new User
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(roleService.getRole(RoleConstant.ROLE_USER.name()));
        user.setVerificationCode(code);
        user.setEnabled(false);
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

        String newPassword = RandomPasswordUtil.random();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", requestDto.getUsername());
        properties.put("newPassword", newPassword);
        sendEmail(user.getEmail(), "Lấy lại mật khẩu", properties, "forgetPassword.html");

        String message = messageSource.getMessage(SuccessMessage.User.FORGET_PASSWORD, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto changePassword(ChangePasswordRequestDto requestDto, String username) {
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new InvalidException(ErrorMessage.INVALID_REPEAT_PASSWORD);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME, username));

        boolean isCorrectPassword = passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword());
        if (!isCorrectPassword) {
            throw new InvalidException(ErrorMessage.Auth.ERR_INCORRECT_PASSWORD);
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

            if (user.isEnabled()) {
                return new CommonResponseDto("Tài khoản đã đã xác thực!");
            }

            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            //Create new Player
            Player player = new Player();
            player.setUser(user);
            playerRepository.save(player);

            return new CommonResponseDto("Tài khoản đã được xác thực thành công!");
        } else {
            return new CommonResponseDto("Mã xác thực không hợp lệ!");
        }
    }

    @Override
    public CommonResponseDto resendConfirmationEmail(String email, String siteURL) {
        // Kiểm tra giới hạn thời gian gửi email
        if (emailRateLimiterService.isMailLimited(email)) {
            return new CommonResponseDto("Bạn đã gửi yêu cầu quá nhiều lần, vui lòng thử lại sau.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.isEnabled()) {
                return new CommonResponseDto("Tài khoản đã xác thực!");
            }

            String code = UUID.randomUUID().toString();
            user.setVerificationCode(code);
            userRepository.save(user);

            emailRateLimiterService.setMailLimit(email, 1, TimeUnit.MINUTES);

            Map<String, Object> properties = new HashMap<>();
            properties.put("url", siteURL + "/verify?code=" + code);
            sendEmail(user.getEmail(), "Kích hoạt tài khoản", properties, "resendConfirmationEmail.html");

            return new CommonResponseDto("Gửi mã xác thực thành công!");
        } else {
            return new CommonResponseDto("Email không hợp lệ!");
        }
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

}
