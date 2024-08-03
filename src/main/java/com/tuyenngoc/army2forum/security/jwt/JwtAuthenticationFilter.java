package com.tuyenngoc.army2forum.security.jwt;

import com.tuyenngoc.army2forum.service.impl.CustomUserDetailsServiceImpl;
import com.tuyenngoc.army2forum.service.impl.JwtTokenServiceImpl;
import com.tuyenngoc.army2forum.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsServiceImpl customUserDetailsService;

    private final JwtTokenProvider tokenProvider;

    private final JwtTokenServiceImpl tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = JwtUtil.getJwtFromRequest(request);

            if (accessToken != null && tokenProvider.validateToken(accessToken)) {
                String userId = tokenProvider.extractSubjectFromJwt(accessToken);

                if (tokenService.isAccessTokenExists(accessToken, userId)) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUserId(userId);
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }

}
