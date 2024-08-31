package com.tuyenngoc.army2forum.domain.dto.response.giftcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuyenngoc.army2forum.constant.CommonConstant;
import com.tuyenngoc.army2forum.domain.dto.common.UserDateAuditingDto;
import com.tuyenngoc.army2forum.domain.entity.GiftCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetGiftCodeResponseDto extends UserDateAuditingDto {

    private long id;

    private String code;

    private short usageLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private LocalDateTime expirationDate;

    public GetGiftCodeResponseDto(GiftCode giftCode) {
        this.setCreatedDate(giftCode.getCreatedDate());
        this.setLastModifiedDate(giftCode.getLastModifiedDate());
        this.setCreatedBy(giftCode.getCreatedBy());
        this.setLastModifiedBy(giftCode.getLastModifiedBy());

        this.id = giftCode.getId();
        this.code = giftCode.getCode();
        this.usageLimit = giftCode.getUsageLimit();
        this.expirationDate = giftCode.getExpirationDate();
    }

}
