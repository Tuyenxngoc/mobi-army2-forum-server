package com.tuyenngoc.army2forum.domain.entity;

import com.tuyenngoc.army2forum.converter.EquipmentChestConverter;
import com.tuyenngoc.army2forum.converter.IntArrayJsonConverter;
import com.tuyenngoc.army2forum.converter.ItemChestConverter;
import com.tuyenngoc.army2forum.domain.entity.common.UserDateAuditing;
import com.tuyenngoc.army2forum.domain.json.EquipChest;
import com.tuyenngoc.army2forum.domain.json.SpecialItemChest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gift_codes",
        uniqueConstraints = @UniqueConstraint(name = "UN_GIFT_CODE_CODE", columnNames = "code"))
public class GiftCode extends UserDateAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_code_id")
    private Long id;

    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(name = "usage_limit", nullable = false)
    private Short usageLimit;

    @Convert(converter = IntArrayJsonConverter.class)
    @Column(name = "used_player_ids", nullable = false)
    private int[] usedPlayerIds = new int[1];

    @Convert(converter = EquipmentChestConverter.class)
    @Column(name = "equips", nullable = false)
    private List<EquipChest> equips = new ArrayList<>();

    @Convert(converter = ItemChestConverter.class)
    @Column(name = "items", nullable = false)
    private List<SpecialItemChest> items = new ArrayList<>();

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "xu")
    private Integer xu;

    @Column(name = "luong")
    private Integer luong;

    @Column(name = "exp")
    private Integer exp;

}
