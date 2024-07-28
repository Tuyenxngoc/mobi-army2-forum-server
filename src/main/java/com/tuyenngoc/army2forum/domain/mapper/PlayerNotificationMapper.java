package com.tuyenngoc.army2forum.domain.mapper;

import com.tuyenngoc.army2forum.domain.dto.request.PlayerNotificationRequestDto;
import com.tuyenngoc.army2forum.domain.entity.PlayerNotification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerNotificationMapper {

    PlayerNotification toPlayerNotification(PlayerNotificationRequestDto requestDto);

}
