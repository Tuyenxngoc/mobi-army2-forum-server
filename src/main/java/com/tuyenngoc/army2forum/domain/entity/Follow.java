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
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", foreignKey = @ForeignKey(name = "FK_FOLLOW_FOLLOWER_ID"), referencedColumnName = "player_id", nullable = false)
    private Player follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id", foreignKey = @ForeignKey(name = "FK_FOLLOW_FOLLOWED_ID"), referencedColumnName = "player_id", nullable = false)
    private Player followed;

}