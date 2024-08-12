package com.tuyenngoc.army2forum.domain.dto.request;

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

    private String lockReason;

}