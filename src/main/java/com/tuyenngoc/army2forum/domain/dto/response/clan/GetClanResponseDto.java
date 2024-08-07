package com.tuyenngoc.army2forum.domain.dto.response.clan;

import com.tuyenngoc.army2forum.domain.dto.common.DateAuditingDto;
import com.tuyenngoc.army2forum.domain.entity.Clan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetClanResponseDto extends DateAuditingDto {

    private long id;

    private int iconId;

    private String icon;

    private String name;

    private String masterName;

    private int memberCount;

    private int memberMax;

    private int cup;

    private int xp;

    private String description;

    private String notification;

    private boolean requireApproval;

    private List<GetClanItemResponseDto> items;

    public GetClanResponseDto(Clan clan) {
        this.setCreatedDate(clan.getCreatedDate());
        this.setLastModifiedDate(clan.getLastModifiedDate());

        this.id = clan.getId();
        this.iconId = clan.getIcon();
        this.icon = String.format("/res/icon/clan/%d.png", clan.getIcon());
        this.name = clan.getName();
        this.masterName = clan.getMaster().getUser().getUsername();
        this.memberCount = clan.getMembers().size();
        this.memberMax = clan.getMemMax();
        this.cup = clan.getCup();
        this.xp = clan.getXp();
        this.description = clan.getDescription();
        this.notification = clan.getNotification();
        this.requireApproval = clan.getRequireApproval();
    }

}
