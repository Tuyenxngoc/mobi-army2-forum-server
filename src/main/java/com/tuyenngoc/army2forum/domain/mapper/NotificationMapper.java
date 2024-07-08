package com.tuyenngoc.army2forum.domain.mapper;

import com.tuyenngoc.army2forum.domain.dto.request.CreateNotificationRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    Notification toNotification(CreateNotificationRequestDto requestDto);

}
