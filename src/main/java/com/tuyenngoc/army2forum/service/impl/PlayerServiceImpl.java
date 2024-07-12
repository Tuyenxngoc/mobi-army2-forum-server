package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Role;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.RoleRepository;
import com.tuyenngoc.army2forum.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    private final RoleRepository roleRepository;

    @Override
    public Player updatePlayerRoles(Long playerId, Long roleId, Collection<? extends GrantedAuthority> authorities) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId));

        Role newRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Role.ERR_NOT_FOUND_ID, roleId));

        // Check if the current user's role is higher than the new role
        if (!canAssignRole(authorities, newRole)) {
            throw new AccessDeniedException("You do not have permission to assign this role.");
        }

        player.getUser().setRole(newRole);

        return playerRepository.save(player);
    }

    private boolean canAssignRole(Collection<? extends GrantedAuthority> authorities, Role newRole) {
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
