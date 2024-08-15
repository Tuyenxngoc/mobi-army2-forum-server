package com.tuyenngoc.army2forum.util;

import com.tuyenngoc.army2forum.security.CustomUserDetails;

import java.util.Arrays;

public class SecurityUtils {

    public static boolean hasRequiredRole(CustomUserDetails userDetails, String[] requiredRoles) {
        if (userDetails == null) {
            return false;
        }
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        Arrays.stream(requiredRoles)
                                .anyMatch(role -> grantedAuthority.getAuthority().equals(role))
                );
    }

}
