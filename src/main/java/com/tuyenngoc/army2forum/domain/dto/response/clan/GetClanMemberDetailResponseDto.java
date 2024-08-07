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
public class GetClanMemberDetailResponseDto {

    private long id;

    private byte rights;

    private int xu;

    private int luong;

    private int xp;

    private int clanPoint;

    private short contributeCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private LocalDateTime contributeTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private LocalDateTime joinTime;

    private String contributeText;

    public GetClanMemberDetailResponseDto(ClanMember clanMember) {
        this.id = clanMember.getId();
        this.rights = clanMember.getRights();
        this.xu = clanMember.getXu();
        this.luong = clanMember.getLuong();
        this.xp = clanMember.getXp();
        this.clanPoint = clanMember.getClanPoint();
        this.contributeCount = clanMember.getContributeCount();
        this.contributeTime = clanMember.getContributeTime();
        this.joinTime = clanMember.getJoinTime();
        this.contributeText = clanMember.getContributeText();
    }

}
