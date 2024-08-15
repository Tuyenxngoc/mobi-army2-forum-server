package com.tuyenngoc.army2forum.domain.dto.request;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import jakarta.validation.constraints.Size;
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

    private LocalDate lockTime;

    @Size(max = 255, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String lockReason;

}