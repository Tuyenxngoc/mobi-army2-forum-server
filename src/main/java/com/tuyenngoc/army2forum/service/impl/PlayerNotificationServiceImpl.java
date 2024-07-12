package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.response.GetPlayerNotificationResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.PlayerNotification;
import com.tuyenngoc.army2forum.repository.PlayerNotificationRepository;
import com.tuyenngoc.army2forum.service.PlayerNotificationService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerNotificationServiceImpl implements PlayerNotificationService {

    private final PlayerNotificationRepository playerNotificationRepository;

    @Override
    public void createNotification(Long playerId, String message) {
        Player player = new Player();
        player.setPlayerId(playerId);

        PlayerNotification notification = new PlayerNotification();
        notification.setPlayer(player);
        notification.setMessage(message);
        playerNotificationRepository.save(notification);
    }

    @Override
    public PaginationResponseDto<GetPlayerNotificationResponseDto> getPlayerNotifications(Long playerId, PaginationRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto);

        Page<GetPlayerNotificationResponseDto> page = playerNotificationRepository.findByPlayerId(playerId, pageable);
        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, page);

        PaginationResponseDto<GetPlayerNotificationResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public PlayerNotification getPlayerNotificationById(Long id, Long playerId) {
        PlayerNotification notification = playerNotificationRepository.findByIdAndPlayerId(id, playerId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found for this player"));

        notification.setRead(true);
        playerNotificationRepository.save(notification);

        return notification;
    }
}
