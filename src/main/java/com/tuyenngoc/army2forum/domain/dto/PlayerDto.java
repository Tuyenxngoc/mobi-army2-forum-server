package com.tuyenngoc.army2forum.domain.dto;

import com.tuyenngoc.army2forum.domain.entity.Player;
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

    private String avatar;

    private String name;

    private boolean isOnline;

    private int points;

    public PlayerDto(Player player) {
        this.id = player.getId();
        this.avatar = String.format("/avatar/%d.gif", player.getNvUsed());
        this.name = player.getUser().getUsername();
        this.isOnline = player.getOnline();
        this.points = player.getComments().size() + player.getPosts().size();
    }
}
