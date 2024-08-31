package com.tuyenngoc.army2forum.domain.dto.response.giftcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuyenngoc.army2forum.constant.CommonConstant;
import com.tuyenngoc.army2forum.domain.entity.PlayerGiftCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlayerGiftCodeResponseDto {

    private long id;

    private String code;

    private long playerId;

    private String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private LocalDateTime redeemTime;

    public GetPlayerGiftCodeResponseDto(PlayerGiftCode playerGiftCode) {
        this.id = playerGiftCode.getId();
        this.code = playerGiftCode.getGiftCode().getCode();
        this.playerId = playerGiftCode.getPlayer().getId();
        this.username = playerGiftCode.getPlayer().getUser().getUsername();
        this.redeemTime = playerGiftCode.getRedeemTime();
    }

}
