package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Role;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.service.PlayerService;
import com.tuyenngoc.army2forum.service.RoleService;
import com.tuyenngoc.army2forum.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    private final RoleService roleService;

    @Override
    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId));
    }

    @Override
    public Player updatePlayerRoles(Long playerId, Long roleId, Collection<? extends GrantedAuthority> authorities) {
        Player player = getPlayerById(playerId);
        Role newRole = roleService.getRole(roleId);

        // Check if the current user's role is higher than the new role
        if (!SecurityUtils.canAssignRole(authorities, newRole)) {
            throw new AccessDeniedException("You do not have permission to assign this role.");
        }

        player.getUser().setRole(newRole);
        return playerRepository.save(player);
    }

}
