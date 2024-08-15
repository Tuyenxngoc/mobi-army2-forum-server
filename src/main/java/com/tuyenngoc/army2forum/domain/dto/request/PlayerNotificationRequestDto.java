package com.tuyenngoc.army2forum.domain.dto.request;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerNotificationRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 255, message = ErrorMessage.INVALID_TEXT_LENGTH)
    @Size(min = 5, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String title;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 255, message = ErrorMessage.INVALID_TEXT_LENGTH)
    @Size(min = 5, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String message;

    private Boolean isPrivate;
}
