package com.tuyenngoc.army2forum.domain.entity;

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
@Table(name = "special_items")
public class SpecialItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "special_item_id")
    private Byte id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "detail", nullable = false, length = 100)
    private String detail;

    @Column(name = "price_xu", nullable = false)
    private int priceXu;

    @Column(name = "price_luong", nullable = false, columnDefinition = "smallint default -1")
    private Short priceLuong = -1;

    @Column(name = "price_sell_xu", nullable = false, columnDefinition = "int default -1")
    private int priceSellXu = -1;

    @Column(name = "expiration_days", nullable = false, columnDefinition = "smallint default 0")
    private Short expirationDays = 0;

    @Column(name = "show_selection", nullable = false, columnDefinition = "tinyint default 1")
    private Byte showSelection = 0;

    @Column(name = "is_on_sale", nullable = false, columnDefinition = "tinyint default 1")
    private Byte isOnSale = 0;

    @Column(name = "type", nullable = false)
    private Byte type;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "ability", nullable = false, length = 50, columnDefinition = "varchar(50) default '[0,0,0,0,0]'")
    private int[] ability;

}