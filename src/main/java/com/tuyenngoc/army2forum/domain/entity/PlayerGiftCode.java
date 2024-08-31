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
@Table(name = "player_gift_codes")
public class PlayerGiftCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_gift_code_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", foreignKey = @ForeignKey(name = "FK_PLAYER_GIFT_CODE_PLAYER_ID"), nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gift_code_id", foreignKey = @ForeignKey(name = "FK_PLAYER_GIFT_CODE_GIFT_CODE_ID"), nullable = false)
    private GiftCode giftCode;

    @Column(name = "redeem_time", nullable = false)
    private LocalDateTime redeemTime;

}
