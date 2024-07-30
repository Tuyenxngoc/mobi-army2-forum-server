package com.tuyenngoc.army2forum.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clans", uniqueConstraints = {
        @UniqueConstraint(name = "UN_CLAN_NAME", columnNames = "name")
})
public class Clan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clan_id")
    private Short clanId;

    @Column(name = "master_id", nullable = false)
    private Integer masterId;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "icon", nullable = false)
    private Short icon;

    @Column(name = "xu", nullable = false)
    private Integer xu = 0;

    @Column(name = "luong", nullable = false)
    private Integer luong = 0;

    @Column(name = "xp", nullable = false)
    private Integer xp = 0;

    @Column(name = "cup", nullable = false)
    private Integer cup = 0;

    @Column(name = "item", length = 1024, nullable = false)
    private String item = "[]";

    @Column(name = "mem", nullable = false)
    private Byte mem = 1;

    @Column(name = "mem_max", nullable = false)
    private Byte memMax = 10;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated = LocalDateTime.of(2024, 1, 1, 0, 0, 0);

    @Column(name = "description", nullable = false)
    private String description;

}
