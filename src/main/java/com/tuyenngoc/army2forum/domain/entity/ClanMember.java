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
@Table(name = "clan_members")
public class ClanMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clan_member_id")
    private Integer clanMemberId;

    @Column(name = "player_id", nullable = false)
    private Integer playerId;

    @Column(name = "rights", nullable = false)
    private Byte rights = 0;

    @Column(name = "join_time", nullable = false)
    private LocalDateTime joinTime = LocalDateTime.of(2024, 1, 1, 0, 0, 0, 0);

    @Column(name = "xu", nullable = false)
    private Integer xu = 0;

    @Column(name = "luong", nullable = false)
    private Integer luong = 0;

    @Column(name = "xp", nullable = false)
    private Integer xp = 0;

    @Column(name = "clan_point", nullable = false)
    private Integer clanPoint = 0;

    @Column(name = "contribute_count", nullable = false)
    private Short contributeCount = 0;

    @Column(name = "contribute_time", nullable = false)
    private LocalDateTime contributeTime = LocalDateTime.of(2024, 1, 1, 0, 0, 0, 0);

    @Column(name = "contribute_text", length = 30, nullable = false)
    private String contributeText = "0 xu";

}
