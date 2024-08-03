package com.tuyenngoc.army2forum.service;

public interface JwtTokenService {

    void saveAccessToken(String accessToken, String userId);

    void saveRefreshToken(String refreshToken, String userId);

    boolean isAccessTokenExists(String accessToken, String userId);

    boolean isRefreshTokenExists(String refreshToken, String userId);

    void deleteTokens(String userId);

}
