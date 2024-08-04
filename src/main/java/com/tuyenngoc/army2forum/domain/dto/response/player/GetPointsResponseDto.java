package com.tuyenngoc.army2forum.domain.dto.response.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPointsResponseDto {

    private int health;

    private int damage;

    private int defense;

    private int luck;

    private int teammates;

    private int totalPoints;

    public GetPointsResponseDto(int[] points, int totalPoints) {
        this.health = points[0];
        this.damage = points[1];
        this.defense = points[2];
        this.luck = points[3];
        this.teammates = points[4];
        this.totalPoints = totalPoints;
    }

}
