package com.tuyenngoc.army2forum.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuyenngoc.army2forum.converter.ClanItemConverter;
import com.tuyenngoc.army2forum.domain.entity.common.DateAuditing;
import com.tuyenngoc.army2forum.domain.json.ClanItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clans", uniqueConstraints = {
        @UniqueConstraint(name = "UN_CLAN_NAME", columnNames = "name"),
        @UniqueConstraint(name = "UN_CLAN_EMAIL", columnNames = "email"),
        @UniqueConstraint(name = "UN_CLAN_MASTER", columnNames = "master_id")
})
public class Clan extends DateAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clan_id")
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "phone_number", length = 20, nullable = false)
    private String phoneNumber;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "icon", nullable = false)
    private Short icon = 1;

    @Column(name = "xu", nullable = false)
    private Integer xu = 0;

    @Column(name = "luong", nullable = false)
    private Integer luong = 0;

    @Column(name = "xp", nullable = false)
    private Integer xp = 0;

    @Column(name = "cup", nullable = false)
    private Integer cup = 0;

    @Column(name = "item", length = 1024, nullable = false)
    @Convert(converter = ClanItemConverter.class)
    private List<ClanItem> item = new ArrayList<>();

    @Column(name = "mem_max", nullable = false)
    private Byte memMax = 10;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "require_approval", nullable = false)
    private Boolean requireApproval = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", foreignKey = @ForeignKey(name = "FK_CLAN_MASTER_ID"), referencedColumnName = "player_id", nullable = false)
    @JsonIgnore
    private Player master;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ClanMember> members;

    @OneToMany(mappedBy = "clan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ClanApproval> pendingApprovals = new ArrayList<>();

}
