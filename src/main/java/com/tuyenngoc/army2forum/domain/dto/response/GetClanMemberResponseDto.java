package com.tuyenngoc.army2forum.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuyenngoc.army2forum.constant.CommonConstant;
import com.tuyenngoc.army2forum.domain.dto.PlayerDto;
import com.tuyenngoc.army2forum.domain.dto.common.DateAuditingDto;
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
public class GetClanMemberResponseDto extends DateAuditingDto {

    private PlayerDto player;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private LocalDateTime joinTime;

    private String rights;

    public GetClanMemberResponseDto(ClanMember clanMember) {
        this.player = new PlayerDto(clanMember.getPlayer());
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
