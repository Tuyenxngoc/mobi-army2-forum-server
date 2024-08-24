package com.tuyenngoc.army2forum.domain.dto.response.player;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuyenngoc.army2forum.constant.CommonConstant;
import com.tuyenngoc.army2forum.domain.dto.ClanDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.service.impl.PlayerServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlayerInfoResponseDto {

    private long id;

    private boolean online;

    private String username;

    private String roleName;

    private String avatar;

    private int xu;

    private int luong;

    private int cup;

    private List<GetPlayerCharacterResponseDto> characters;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClanDto clan;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isChestLocked;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isInvitationLocked;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phoneNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private LocalDateTime x2XpTime;

    public GetPlayerInfoResponseDto(Player player) {
        this.id = player.getId();
        this.online = player.getIsOnline();
        this.username = player.getUser().getUsername();
        this.roleName = player.getUser().getRole().getName();
        this.avatar = PlayerServiceImpl.getAvatar(this.username, player.getActiveCharacter().getCharacter().getId());
        this.xu = player.getXu();
        this.luong = player.getLuong();
        this.cup = player.getCup();
        this.characters = player.getPlayerCharacters().stream()
                .map(GetPlayerCharacterResponseDto::new)
                .collect(Collectors.toList());
        if (player.getClanMember() != null) {
            this.clan = new ClanDto(player.getClanMember().getClan());
        }
    }

}
