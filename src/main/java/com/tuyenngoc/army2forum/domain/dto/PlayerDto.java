package com.tuyenngoc.army2forum.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuyenngoc.army2forum.domain.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Files;
import java.nio.file.Paths;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClanMemberDto clanMember;

    public PlayerDto(Player player) {
        this.id = player.getId();
        this.name = player.getUser().getUsername();
        this.isOnline = player.getIsOnline();
        this.points = player.getComments().size() + player.getPosts().size();

        String avatarPath = String.format("/avatar/%s_%d.gif", this.name, player.getActiveCharacter().getCharacter().getId());
        if (Files.exists(Paths.get("src/main/resources/static" + avatarPath))) {
            this.avatar = avatarPath;
        } else {
            this.avatar = "/avatar/default.png";
        }
    }
}
