package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPlayerNotificationResponseDto;
import com.tuyenngoc.army2forum.domain.entity.PlayerNotification;

public interface PlayerNotificationService {

    void createNotification(Long playerId, String message);

    PaginationResponseDto<GetPlayerNotificationResponseDto> getPlayerNotifications(Long playerId, PaginationRequestDto requestDto);

    PlayerNotification getPlayerNotificationById(Long id, Long playerId);

}
