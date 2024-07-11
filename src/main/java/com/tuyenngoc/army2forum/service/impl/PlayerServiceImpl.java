package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePlayerScheduleRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Role;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.RoleRepository;
import com.tuyenngoc.army2forum.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    private final RoleRepository roleRepository;

    @Override
    public Player updatePlayerRoles(Long playerId, Long roleId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Role.ERR_NOT_FOUND_ID, roleId));

        player.getUser().setRole(role);

        return playerRepository.save(player);
    }

    @Override
    public void updatePlayerSchedule(Long playerId, UpdatePlayerScheduleRequestDto requestDto) {

    }

}
