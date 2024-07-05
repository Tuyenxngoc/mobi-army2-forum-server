package com.tuyenngoc.army2forum.service;

public interface JwtTokenService {

    void saveAccessToken(String accessToken, String username);

    void saveRefreshToken(String refreshToken, String username);

    boolean isAccessTokenExists(String accessToken, String username);

    boolean isRefreshTokenExists(String refreshToken, String username);

    void deleteTokens(String username);

}
