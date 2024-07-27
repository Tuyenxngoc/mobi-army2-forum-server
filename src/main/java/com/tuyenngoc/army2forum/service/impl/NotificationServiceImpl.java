package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.request.NotificationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Notification;
import com.tuyenngoc.army2forum.domain.mapper.NotificationMapper;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.NotificationRepository;
import com.tuyenngoc.army2forum.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final MessageSource messageSource;

    private final NotificationMapper notificationMapper;

    @Override
    public Notification createNotification(NotificationRequestDto requestDto) {
        Notification notification = notificationMapper.toNotification(requestDto);

        return notificationRepository.save(notification);
    }

    @Override
    public Notification updateNotification(Long notificationId, NotificationRequestDto requestDto) {
        Notification notification = getNotificationById(notificationId);

        notification.setTitle(requestDto.getTitle());
        notification.setContent(requestDto.getContent());

        return notificationRepository.save(notification);
    }

    @Override
    public CommonResponseDto deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public Notification getNotificationById(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Notification.ERR_NOT_FOUND_ID, notificationId));
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAllByOrderByLastModifiedDateDesc();
    }

}
