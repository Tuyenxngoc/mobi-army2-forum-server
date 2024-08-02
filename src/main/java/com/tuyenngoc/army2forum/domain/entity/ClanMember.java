package com.tuyenngoc.army2forum.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuyenngoc.army2forum.constant.ClanMemberRightsConstants;
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
@Table(name = "clan_members", uniqueConstraints = {
        @UniqueConstraint(name = "UN_CLAN_PLAYER", columnNames = "player_id"),
})
public class ClanMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clan_member_id")
    private Long id;

    @Column(name = "rights", nullable = false)
    private Byte rights = ClanMemberRightsConstants.CLAN_MEMBER;

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

    @Column(name = "contribute_time")
    private LocalDateTime contributeTime;

    @Column(name = "join_time", nullable = false, updatable = false)
    private LocalDateTime joinTime;

    @Column(name = "contribute_text", length = 30, nullable = false)
    private String contributeText = "0 xu";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", foreignKey = @ForeignKey(name = "FK_CLAN_MEMBER_PLAYER_ID"), referencedColumnName = "player_id", nullable = false)
    @JsonIgnore
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clan_id", foreignKey = @ForeignKey(name = "FK_CLAN_MEMBER_CLAN_ID"), referencedColumnName = "clan_id", nullable = false)
    @JsonIgnore
    private Clan clan;

}
