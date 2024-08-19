package com.tuyenngoc.army2forum.domain.dto.response.player;

import com.tuyenngoc.army2forum.domain.entity.PlayerCharacters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.file.Files;
import java.nio.file.Paths;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlayerCharacterResponseDto {

    private long id;

    private String name;

    private String avatar;

    private int xp = 0;

    private int level = 1;

    private int points = 0;

    private int[] additionalPoints;

    private int[] data;

    public GetPlayerCharacterResponseDto(PlayerCharacters playerCharacters) {
        this.id = playerCharacters.getCharacter().getId();
        this.name = playerCharacters.getCharacter().getName();
        this.xp = playerCharacters.getXp();
        this.level = playerCharacters.getLevel();
        this.points = playerCharacters.getPoints();
        this.additionalPoints = playerCharacters.getAdditionalPoints();
        this.data = playerCharacters.getData();

        String avatarPath = String.format("/images/avatar/%s_%d.gif", playerCharacters.getPlayer().getUser().getUsername(), this.id);
        if (Files.exists(Paths.get("public" + avatarPath))) {
            this.avatar = avatarPath;
        } else {
            this.avatar = "/images/avatar/default.png";
        }
    }

}
