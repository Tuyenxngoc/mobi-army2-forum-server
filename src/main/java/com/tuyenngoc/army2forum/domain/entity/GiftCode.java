package com.tuyenngoc.army2forum.domain.entity;

import com.tuyenngoc.army2forum.domain.entity.common.UserDateAuditing;
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

    @Column(name = "used_player_ids", nullable = false)
    private String usedPlayerIds;

    @Column(name = "equips", nullable = false)
    private String equips;

    @Column(name = "items", nullable = false)
    private String items;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "xu")
    private Integer xu;

    @Column(name = "luong")
    private Integer luong;

    @Column(name = "exp")
    private Integer exp;

}
