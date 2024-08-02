package com.tuyenngoc.army2forum.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "characters",
        uniqueConstraints = @UniqueConstraint(name = "UN_CHARACTER_NAME", columnNames = "name"))
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private Byte id;

    @Column(name = "name", nullable = false, length = 15)
    private String name;

    @Column(name = "xu", nullable = false)
    private Integer xu = 0;

    @Column(name = "luong", nullable = false)
    private Short luong = 0;

    @Column(name = "wind_resistance", nullable = false)
    private Short windResistance = 0;

    @Column(name = "min_angle", nullable = false)
    private Short minAngle = 0;

    @Column(name = "damage", nullable = false)
    private Short damage = 0;

    @Column(name = "bullet_damage", nullable = false)
    private Short bulletDamage = 0;

    @Column(name = "bullet_count", nullable = false)
    private Short bulletCount = 0;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PlayerCharacters> playerCharacters = new ArrayList<>();

    public Character(String name, int xu, int luong, int windResistance, int minAngle, int damage, int bulletDamage, int bulletCount) {
        this.name = name;
        this.xu = xu;
        this.luong = (short) luong;
        this.windResistance = (short) windResistance;
        this.minAngle = (short) minAngle;
        this.damage = (short) damage;
        this.bulletDamage = (short) bulletDamage;
        this.bulletCount = (short) bulletCount;
    }

}
