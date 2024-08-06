package com.tuyenngoc.army2forum.domain.entity;

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
@Table(name = "clan_shops")
public class ClanShop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clan_shop_id")
    private Byte clanShopId;

    @Column(name = "level", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Byte level = 1;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "time", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Byte time = 0;

    @Column(name = "on_sale", nullable = false, columnDefinition = "TINYINT DEFAULT 1")
    private Byte onSale = 1;

    @Column(name = "xu", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer xu = 0;

    @Column(name = "luong", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer luong = 0;

}