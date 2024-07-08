package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.request.CreateNotificationRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Notification;
import com.tuyenngoc.army2forum.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@Tag(name = "Notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Create Notification")
    @PostMapping(UrlConstant.Notification.CREATE)
    public ResponseEntity<?> createNotification( @Valid @RequestBody CreateNotificationRequestDto requestDto) {
        return VsResponseUtil.success(notificationService.createNotification(requestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Update Notification")
    @PutMapping(UrlConstant.Notification.UPDATE)
    public ResponseEntity<?> updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody CreateNotificationRequestDto requestDto
    ) {
        return VsResponseUtil.success(notificationService.updateNotification(id, requestDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Delete Notification")
    @DeleteMapping(UrlConstant.Notification.DELETE)
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return VsResponseUtil.success(null);
    }

    @Operation(summary = "API Get Notification by Id")
    @GetMapping(UrlConstant.Notification.GET_BY_ID)
    public ResponseEntity<?> getNotificationById(@PathVariable Long id) {
        return VsResponseUtil.success(notificationService.getNotificationById(id));
    }

    @Operation(summary = "API Get All Notifications")
    @GetMapping(UrlConstant.Notification.GET_ALL)
    public ResponseEntity<?> getAllNotifications() {
        return VsResponseUtil.success(notificationService.getAllNotifications());
    }

}
