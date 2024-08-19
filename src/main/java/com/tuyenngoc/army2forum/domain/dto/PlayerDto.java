package com.tuyenngoc.army2forum.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.service.impl.PlayerServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {

    private long id;

    private String name;

    private String avatar;

    private boolean isOnline;

    private int points;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClanMemberDto clanMember;

    public PlayerDto(Player player) {
        this.id = player.getId();
        this.name = player.getUser().getUsername();
        this.avatar = PlayerServiceImpl.getAvatar(this.name, player.getActiveCharacter().getCharacter().getId());
        this.isOnline = player.getIsOnline();
        this.points = player.getComments().size() + player.getPosts().size();
    }
}
