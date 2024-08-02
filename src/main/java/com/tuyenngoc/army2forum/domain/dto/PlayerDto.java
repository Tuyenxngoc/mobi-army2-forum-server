package com.tuyenngoc.army2forum.domain.dto;

import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.PlayerCharacters;
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

        PlayerCharacters usedCharacter = player.getPlayerCharacters().stream()
                .filter(PlayerCharacters::getIsUsed)
                .findFirst()
                .orElse(null);

        if (usedCharacter != null) {
            this.avatar = String.format("/avatar/%d.gif", usedCharacter.getId());
        } else {
            this.avatar = "/avatar/1.gif";
        }

        this.name = player.getUser().getUsername();
        this.isOnline = player.getIsOnline();
        this.points = player.getComments().size() + player.getPosts().size();
    }
}
