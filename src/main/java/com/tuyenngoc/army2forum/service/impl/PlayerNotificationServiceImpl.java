package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.RoleConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.PlayerNotificationRequestDto;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerNotificationServiceImpl implements PlayerNotificationService {

    PlayerNotificationRepository playerNotificationRepository;

    PlayerRepository playerRepository;

    MessageSource messageSource;

    PlayerNotificationMapper playerNotificationMapper;

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
    public CommonResponseDto createPlayerNotification(PlayerNotificationRequestDto requestDto) {
        List<Long> playerIds;

        if (requestDto.getIsPrivate()) {
            List<String> roleNames = List.of(RoleConstant.ROLE_SUPER_ADMIN.name(), RoleConstant.ROLE_ADMIN.name(), RoleConstant.ROLE_MODERATOR.name());
            playerIds = playerRepository.findPlayerIdsByRoleNames(roleNames);
        } else {
            playerIds = playerRepository.findAllPlayerIds();
        }

        savePlayerNotifications(requestDto, playerIds);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    private void savePlayerNotifications(PlayerNotificationRequestDto requestDto, List<Long> playerIds) {
        for (Long playerId : playerIds) {
            PlayerNotification playerNotification = playerNotificationMapper.toPlayerNotification(requestDto);
            playerNotification.setPlayer(new Player(playerId));
            playerNotificationRepository.save(playerNotification);
        }
    }

}
