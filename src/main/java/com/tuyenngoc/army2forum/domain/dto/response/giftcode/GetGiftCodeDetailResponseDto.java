package com.tuyenngoc.army2forum.domain.dto.response.giftcode;

import com.tuyenngoc.army2forum.domain.dto.common.UserDateAuditingDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.GetEquipmentResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.GetSpecialItemResponseDto;
import com.tuyenngoc.army2forum.domain.entity.GiftCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetGiftCodeDetailResponseDto extends UserDateAuditingDto {

    private long id;

    private String code;

    private short usageLimit;

    private int usageCount;

    private int xu;

    private int luong;

    private int exp;

    private LocalDateTime expirationDate;

    private List<GetEquipmentResponseDto> equips = new ArrayList<>();

    private List<GetSpecialItemResponseDto> items = new ArrayList<>();

    public GetGiftCodeDetailResponseDto(GiftCode giftCode) {
        this.setCreatedDate(giftCode.getCreatedDate());
        this.setLastModifiedDate(giftCode.getLastModifiedDate());
        this.setCreatedBy(giftCode.getCreatedBy());
        this.setLastModifiedBy(giftCode.getLastModifiedBy());

        this.id = giftCode.getId();
        this.code = giftCode.getCode();
        this.usageLimit = giftCode.getUsageLimit();
        this.usageCount = giftCode.getPlayerGiftCodes().size();
        this.expirationDate = giftCode.getExpirationDate();
        this.xu = giftCode.getXu();
        this.luong = giftCode.getLuong();
        this.exp = giftCode.getExp();
    }

}
