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

    private Long id;

    private String avatar;

    private String name;

    private boolean isOnline;

    private int points;

    public PlayerDto(Player player) {
        this.id = player.getId();
        this.avatar = "";
        this.name = player.getUser().getUsername();
        this.isOnline = player.isOnline();
        this.points = player.getComments().size() + player.getPosts().size();
    }
}
