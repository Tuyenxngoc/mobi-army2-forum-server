package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.request.UpdatePlayerScheduleRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Player;

public interface PlayerService {

    Player updatePlayerRoles(Long playerId, Long roleId);

    void updatePlayerSchedule(Long playerId, UpdatePlayerScheduleRequestDto requestDto);

}
