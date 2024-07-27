package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.request.NotificationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Notification;

import java.util.List;

public interface NotificationService {

    Notification createNotification(NotificationRequestDto requestDto);

    Notification updateNotification(Long notificationId, NotificationRequestDto requestDto);

    CommonResponseDto deleteNotification(Long notificationId);

    Notification getNotificationById(Long notificationId);

    List<Notification> getAllNotifications();

}
