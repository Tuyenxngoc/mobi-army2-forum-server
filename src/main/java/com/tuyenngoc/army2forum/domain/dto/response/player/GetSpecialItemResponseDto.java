package com.tuyenngoc.army2forum.domain.dto.response.player;

import com.tuyenngoc.army2forum.domain.entity.SpecialItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetSpecialItemResponseDto {

    private long id;

    private String name;

    private String detail;

    private String imageUrl;

    private short quantity;

    public GetSpecialItemResponseDto(SpecialItem item) {
        this.id = item.getId();
        this.name = item.getName();
        this.detail = item.getDetail();
        this.imageUrl = String.format("/res/icon/item/%d.png", item.getId());
    }

}
