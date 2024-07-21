package com.tuyenngoc.army2forum.util;

import com.tuyenngoc.army2forum.domain.entity.Role;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

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

    public static boolean canAssignRole(Collection<? extends GrantedAuthority> authorities, Role newRole) {
        boolean isSuperAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_SUPER_ADMIN"));
        if (isSuperAdmin) {
            return true; // Super Admin can assign any role
        }

        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return !newRole.getName().equals("ROLE_SUPER_ADMIN") && !newRole.getName().equals("ROLE_ADMIN");
        }

        return false;
    }
}
