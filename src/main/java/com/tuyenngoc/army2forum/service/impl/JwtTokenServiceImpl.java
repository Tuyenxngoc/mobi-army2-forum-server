package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN_";
    private static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN_";

    @Value("${jwt.access.expiration_time}")
    private Integer EXPIRATION_TIME_ACCESS_TOKEN;

    @Value("${jwt.refresh.expiration_time}")
    private Integer EXPIRATION_TIME_REFRESH_TOKEN;

    private final RedisTemplate<String, Object> redisTemplate;

    private String getAccessTokenKey(String username) {
        return ACCESS_TOKEN_KEY + username.toUpperCase();
    }

    private String getRefreshTokenKey(String username) {
        return REFRESH_TOKEN_KEY + username.toUpperCase();
    }

    @Override
    public void saveAccessToken(String accessToken, String username) {
        redisTemplate.opsForValue().set(getAccessTokenKey(username), accessToken, EXPIRATION_TIME_ACCESS_TOKEN, TimeUnit.MINUTES);
    }

    @Override
    public void saveRefreshToken(String refreshToken, String username) {
        redisTemplate.opsForValue().set(getRefreshTokenKey(username), refreshToken, EXPIRATION_TIME_REFRESH_TOKEN, TimeUnit.MINUTES);
    }

    @Override
    public boolean isAccessTokenExists(String accessToken, String username) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getAccessTokenKey(username))) &&
                accessToken.equals(redisTemplate.opsForValue().get(getAccessTokenKey(username)));
    }

    @Override
    public boolean isRefreshTokenExists(String refreshToken, String username) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getRefreshTokenKey(username))) &&
                refreshToken.equals(redisTemplate.opsForValue().get(getRefreshTokenKey(username)));
    }

    @Override
    public void deleteTokens(String username) {
        redisTemplate.delete(getAccessTokenKey(username));
        redisTemplate.delete(getRefreshTokenKey(username));
    }
}
