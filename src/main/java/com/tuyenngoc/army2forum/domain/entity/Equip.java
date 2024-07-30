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
@Table(name = "equips")
public class Equip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equip_id", nullable = false)
    private Short equipId;

    @Column(name = "character_id", nullable = false)
    private Byte characterId;

    @Column(name = "equip_type", nullable = false)
    private Byte equipType;

    @Column(name = "equip_index", nullable = false)
    private Short equipIndex;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "price_xu", nullable = false)
    private Integer priceXu;

    @Column(name = "price_luong", nullable = false, columnDefinition = "smallint default -1")
    private Short priceLuong = -1;

    @Column(name = "sale_price_xu", nullable = false, columnDefinition = "int default -1")
    private Integer salePriceXu = -1;

    @Column(name = "is_disguise", nullable = false, columnDefinition = "tinyint default 0")
    private Byte isDisguise = 0;

    @Column(name = "disguise_equipped_indexes", length = 30)
    private String disguiseEquippedIndexes;

    @Column(name = "expiration_days", nullable = false, columnDefinition = "tinyint default 0")
    private Byte expirationDays = 0;

    @Column(name = "level_requirement", nullable = false, columnDefinition = "smallint default 0")
    private Short levelRequirement = 0;

    @Column(name = "frame_count", nullable = false, columnDefinition = "int default 0")
    private Integer frameCount = 0;

    @Column(name = "bullet_id", nullable = false, columnDefinition = "smallint default 0")
    private Short bulletId = 0;

    @Column(name = "on_sale", nullable = false, columnDefinition = "tinyint default 1")
    private Byte onSale = 1;

    @Column(name = "is_default", nullable = false, columnDefinition = "tinyint(1) default 0")
    private Boolean isDefault = false;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "big_image_cut_x", nullable = false, length = 50)
    private int[] bigImageCutX;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "big_image_cut_y", nullable = false, length = 50)
    private int[] bigImageCutY;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "big_image_size_x", nullable = false, length = 50)
    private int[] bigImageSizeX;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "big_image_size_y", nullable = false, length = 50)
    private int[] bigImageSizeY;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "big_image_align_x", nullable = false, length = 50)
    private int[] bigImageAlignX;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "big_image_align_y", nullable = false, length = 50)
    private int[] bigImageAlignY;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "additional_points", length = 50)
    private int[] additionalPoints;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "additional_percent", length = 50)
    private int[] additionalPercent;

}
