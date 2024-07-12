package com.tuyenngoc.army2forum.domain.dto.response;

import com.tuyenngoc.army2forum.domain.dto.common.DateAuditingDto;
import com.tuyenngoc.army2forum.domain.entity.PlayerNotification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlayerNotificationResponseDto extends DateAuditingDto {

    private Long id;

    private String message;

    private boolean isRead;

    public GetPlayerNotificationResponseDto(PlayerNotification playerNotification) {
        this.id = playerNotification.getId();
        this.message = playerNotification.getMessage();
        this.isRead = playerNotification.isRead();
        this.setCreatedDate(playerNotification.getCreatedDate());
        this.setLastModifiedDate(playerNotification.getLastModifiedDate());
    }
}
