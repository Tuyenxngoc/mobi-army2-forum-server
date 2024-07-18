package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.dto.request.CreateNotificationRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Notification;
import com.tuyenngoc.army2forum.domain.mapper.NotificationMapper;
import com.tuyenngoc.army2forum.repository.NotificationRepository;
import com.tuyenngoc.army2forum.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    @Override
    public Notification createNotification(CreateNotificationRequestDto requestDto) {
        Notification notification = notificationMapper.toNotification(requestDto);

        return notificationRepository.save(notification);
    }

    @Override
    public Notification updateNotification(Long id, CreateNotificationRequestDto requestDto) {
        Notification notification = notificationMapper.toNotification(requestDto);

        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        existingNotification.setTitle(notification.getTitle());
        existingNotification.setContent(notification.getContent());
        return notificationRepository.save(existingNotification);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAllByOrderByLastModifiedDateDesc();
    }

}
