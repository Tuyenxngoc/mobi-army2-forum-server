package com.tuyenngoc.army2forum.domain.dto.response.auth;

import com.tuyenngoc.army2forum.constant.CommonConstant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
public class LoginResponseDto {

    private String tokenType = CommonConstant.TOKEN_TYPE;

    private String accessToken;

    private String refreshToken;

    private Collection<? extends GrantedAuthority> authorities;

    public LoginResponseDto(String accessToken, String refreshToken, Collection<? extends GrantedAuthority> authorities) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.authorities = authorities;
    }
}
