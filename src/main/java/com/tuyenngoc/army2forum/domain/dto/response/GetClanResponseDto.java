package com.tuyenngoc.army2forum.domain.dto.response;

import com.tuyenngoc.army2forum.domain.dto.common.DateAuditingDto;
import com.tuyenngoc.army2forum.domain.entity.Clan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetClanResponseDto extends DateAuditingDto {

    private long id;

    private String icon;

    private String name;

    private String masterName;

    private int memberCount;

    private int memberMax;

    public GetClanResponseDto(Clan clan) {
        this.setCreatedDate(clan.getCreatedDate());
        this.setLastModifiedDate(clan.getLastModifiedDate());

        this.id = clan.getId();
        this.icon = String.format("/res/icon/clan/%d.png", clan.getIcon());
        this.name = clan.getName();
        this.masterName = clan.getMaster().getUser().getUsername();
        this.memberCount = clan.getMembers().size();
        this.memberMax = clan.getMemMax();
    }

}
