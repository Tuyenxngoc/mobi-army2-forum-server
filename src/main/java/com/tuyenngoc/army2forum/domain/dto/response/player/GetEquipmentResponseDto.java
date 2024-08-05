package com.tuyenngoc.army2forum.domain.dto.response.player;

import com.tuyenngoc.army2forum.domain.entity.Equip;
import com.tuyenngoc.army2forum.domain.json.EquipChest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetEquipmentResponseDto {

    private String name;

    private String imageUrl;

    private int[] points;

    private int[] percents;

    private List<String> slots = new ArrayList<>();

    public GetEquipmentResponseDto(Equip equip, EquipChest equipChest) {
        this.name = equip.getName();
        this.points = equipChest.getAddPoints();
        this.percents = equipChest.getAddPercents();

        for (int slot : equipChest.getSlots()) {
            if (slot < 0) {
                slots.add(null);
            } else {
                slots.add("/res/icon/item/" + slot + ".png");
            }
        }
    }

}
