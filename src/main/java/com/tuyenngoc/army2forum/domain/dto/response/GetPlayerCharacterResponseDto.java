package com.tuyenngoc.army2forum.domain.dto.response;

import com.tuyenngoc.army2forum.domain.entity.PlayerCharacters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetPlayerCharacterResponseDto {

    private long id;

    private String name;

    private String avatar;

    private boolean isUsed = false;

    private int xp = 0;

    private int level = 1;

    private int points = 0;

    private int[] additionalPoints;

    private int[] data;

    public GetPlayerCharacterResponseDto(PlayerCharacters playerCharacters) {
        this.id = playerCharacters.getCharacter().getId();
        this.name = playerCharacters.getCharacter().getName();
        this.avatar = String.format("/avatar/%d.gif", this.id);
        this.isUsed = playerCharacters.getIsUsed();
        this.xp = playerCharacters.getXp();
        this.level = playerCharacters.getLevel();
        this.points = playerCharacters.getPoints();
        this.additionalPoints = playerCharacters.getAdditionalPoints();
        this.data = playerCharacters.getData();
    }

}
