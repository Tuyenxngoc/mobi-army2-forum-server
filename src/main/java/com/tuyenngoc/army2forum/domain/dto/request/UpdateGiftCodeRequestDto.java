package com.tuyenngoc.army2forum.domain.dto.request;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGiftCodeRequestDto {

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    @Min(value = 0, message = ErrorMessage.INVALID_MINIMUM_ZERO)
    @Max(value = 32000, message = ErrorMessage.INVALID_MAXIMUM_SHORT)
    private Short usageLimit;

    private LocalDateTime expirationDate;

}
