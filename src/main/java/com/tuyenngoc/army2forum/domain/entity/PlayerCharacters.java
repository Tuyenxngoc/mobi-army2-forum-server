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

    @Column(name = "xp", nullable = false, columnDefinition = "int default 0")
    private Integer xp = 0;

    @Column(name = "level", nullable = false, columnDefinition = "int default 1")
    private Integer level = 1;

    @Column(name = "points", nullable = false, columnDefinition = "int default 0")
    private Integer points = 0;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "additional_points", nullable = false, columnDefinition = "varchar(100) default '[0,0,10,10,10]'")
    private int[] additionalPoints = new int[]{0, 0, 10, 10, 10};

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "data", nullable = false, columnDefinition = "varchar(100) default '[-1,-1,-1,-1,-1,-1]'")
    private int[] data = new int[]{-1, -1, -1, -1, -1, -1};

    @OneToOne(mappedBy = "activeCharacter", cascade = CascadeType.ALL)
    @JsonIgnore
    private Player activePlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", foreignKey = @ForeignKey(name = "FK_PLAYER_CHARACTERS_PLAYER_ID"), referencedColumnName = "player_id", nullable = false)
    @JsonIgnore
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", foreignKey = @ForeignKey(name = "FK_PLAYER_CHARACTERS_CHARACTER_ID"), referencedColumnName = "character_id", nullable = false)
    @JsonIgnore
    private Character character;

}
