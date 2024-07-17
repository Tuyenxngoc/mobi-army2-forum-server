package com.tuyenngoc.army2forum.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuyenngoc.army2forum.domain.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final String userId;

    @Getter
    private final Long playerId;

    @Getter
    private final String email;

    @JsonIgnore
    private final String username;

    @JsonIgnore
    private final String password;

    private final boolean isEnabled;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String userId, Long playerId, String email, String username, String password, boolean isEnabled, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.playerId = playerId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
    }

    public static CustomUserDetails create(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return new CustomUserDetails(user.getId(), user.getPlayer().getId(), user.getEmail(), user.getUsername(), user.getPassword(), user.isEnabled(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
