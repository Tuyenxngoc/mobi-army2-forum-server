package com.tuyenngoc.army2forum.domain.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuyenngoc.army2forum.constant.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public abstract class DateAuditingDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private LocalDateTime lastModifiedDate;

}
