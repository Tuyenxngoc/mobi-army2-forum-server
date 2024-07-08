package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.request.CreateNotificationRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Notification;

import java.util.List;

public interface NotificationService {

    Notification createNotification(CreateNotificationRequestDto requestDto);

    Notification updateNotification(Long id, CreateNotificationRequestDto requestDto);

    void deleteNotification(Long id);

    Notification getNotificationById(Long id);

    List<Notification> getAllNotifications();

}
