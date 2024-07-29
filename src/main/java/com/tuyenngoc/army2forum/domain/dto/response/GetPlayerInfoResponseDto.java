package com.tuyenngoc.army2forum.domain.dto.response;

import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.SpecialItem;
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

    private List<SpecialItem> itemChest;

    public GetPlayerInfoResponseDto(Player player) {
        this.online = player.isOnline();
        this.xu = player.getXu();
        this.luong = player.getLuong();
        this.itemChest = player.getItemChest();
    }

}
