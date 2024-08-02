package com.tuyenngoc.army2forum.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuyenngoc.army2forum.converter.IntArrayJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "player_characters")
public class PlayerCharacters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_character_id")
    private Long id;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;

    @Column(name = "xp", nullable = false)
    private Integer xp = 0;

    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Column(name = "points", nullable = false)
    private Integer points = 0;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "additional_points", nullable = false, columnDefinition = "varchar(1000) default '[]'")
    private int[] additionalPoints;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "data", nullable = false, columnDefinition = "varchar(1000) default '[]'")
    private int[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", foreignKey = @ForeignKey(name = "FK_PLAYER_CHARACTERS_PLAYER_ID"), referencedColumnName = "player_id", nullable = false)
    @JsonIgnore
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", foreignKey = @ForeignKey(name = "FK_PLAYER_CHARACTERS_CHARACTER_ID"), referencedColumnName = "character_id", nullable = false)
    @JsonIgnore
    private Character character;

}
