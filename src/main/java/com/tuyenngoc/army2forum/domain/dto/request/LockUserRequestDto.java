package com.tuyenngoc.army2forum.domain.dto.request;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LockUserRequestDto {

    @NotNull(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    private LocalDate lockTime;

}