package com.tuyenngoc.army2forum.domain.dto;

import com.tuyenngoc.army2forum.domain.entity.Clan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClanDto {

    private long id;

    private String name;

    private String icon;

    public ClanDto(Clan clan) {
        this.id = clan.getId();
        this.name = clan.getName();
        this.icon = String.format("/images/icon/clan/%d.png", clan.getIcon());
    }
}
