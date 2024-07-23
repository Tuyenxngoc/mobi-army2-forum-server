package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.CreatePlayerNotificationDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPlayerNotificationResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.PlayerNotification;
import com.tuyenngoc.army2forum.domain.mapper.PlayerNotificationMapper;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.PlayerNotificationRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.service.PlayerNotificationService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerNotificationServiceImpl implements PlayerNotificationService {

    private final PlayerNotificationRepository playerNotificationRepository;

    private final PlayerRepository playerRepository;

    private final MessageSource messageSource;

    private final PlayerNotificationMapper playerNotificationMapper;

    @Override
    public void createNotification(Long playerId, String title, String message) {
        Player player = new Player();
        player.setId(playerId);

        PlayerNotification notification = new PlayerNotification();
        notification.setPlayer(player);
        notification.setTitle(title);
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
                .orElseThrow(() -> new NotFoundException(ErrorMessage.PlayerNotification.ERR_NOT_FOUND_ID, id));

        notification.setRead(true);
        playerNotificationRepository.save(notification);

        return notification;
    }

    @Override
    @Transactional
    public CommonResponseDto deletePlayerNotificationById(Long id, Long playerId) {
        playerNotificationRepository.deleteByIdAndPlayerId(id, playerId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto createPlayerNotification(CreatePlayerNotificationDto createPlayerNotificationDto) {
        List<Player> players = playerRepository.findAll();
        for (Player player : players) {
            PlayerNotification notification = playerNotificationMapper.toPlayerNotification(createPlayerNotificationDto);

            notification.setPlayer(player);
            playerNotificationRepository.save(notification);
        }

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }
}
