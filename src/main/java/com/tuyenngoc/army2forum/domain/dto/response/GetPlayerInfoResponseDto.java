package com.tuyenngoc.army2forum.domain.dto.response;

import com.tuyenngoc.army2forum.domain.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlayerInfoResponseDto {

    private boolean online;

    private int xu;

    private int luong;

    private List<GetSpecialItemResponseDto> itemChest;

    private List<GetEquipmentResponseDto> equipChest;

    public GetPlayerInfoResponseDto(Player player) {
        this.online = player.isOnline();
        this.xu = player.getXu();
        this.luong = player.getLuong();
    }

}
