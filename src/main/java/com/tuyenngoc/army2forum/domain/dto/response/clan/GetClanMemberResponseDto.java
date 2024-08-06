package com.tuyenngoc.army2forum.domain.dto.response.clan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuyenngoc.army2forum.constant.CommonConstant;
import com.tuyenngoc.army2forum.domain.entity.ClanMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetClanMemberResponseDto {

    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private LocalDateTime joinTime;

    private String rights;

    public GetClanMemberResponseDto(ClanMember clanMember) {
        this.name = clanMember.getPlayer().getUser().getUsername();
        this.joinTime = clanMember.getJoinTime();
        this.rights = getRightsName(clanMember.getRights());
    }

    private String getRightsName(Byte rights) {
        return switch (rights) {
            case 2 -> "Đội trưởng";
            case 1 -> "Hội phó";
            default -> "Thành viên";
        };
    }

}
