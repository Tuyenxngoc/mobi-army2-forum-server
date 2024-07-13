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

    private String avatar;

    private String name;

    private boolean isOnline;

    public PlayerDto(Player player) {
        this.avatar = "";
        this.name = player.getUser().getUsername();
        this.isOnline = player.isOnline();
    }
}
