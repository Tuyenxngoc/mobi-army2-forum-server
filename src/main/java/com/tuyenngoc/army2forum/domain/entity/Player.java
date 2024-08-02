package com.tuyenngoc.army2forum.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuyenngoc.army2forum.converter.EquipmentChestConverter;
import com.tuyenngoc.army2forum.converter.ItemChestConverter;
import com.tuyenngoc.army2forum.domain.entity.common.DateAuditing;
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
@Table(name = "players",
        uniqueConstraints = {
                @UniqueConstraint(name = "UN_PLAYER_USER_ID", columnNames = "user_id"),
                @UniqueConstraint(name = "UN_PLAYER_ACTIVE_CHARACTER_ID", columnNames = "active_character_id")
        }
)
public class Player extends DateAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline = false;

    @Column(name = "xu", nullable = false)
    private Integer xu = 1000;

    @Column(name = "luong", nullable = false)
    private Integer luong = 0;

    @Column(name = "x2_xp_time")
    private LocalDateTime x2XpTime;

    @Column(name = "item_chest", columnDefinition = "varchar(1000) default '[]'")
    @Convert(converter = ItemChestConverter.class)
    private List<SpecialItemChest> itemChest = new ArrayList<>();

    @Column(name = "equipment_chest", columnDefinition = "varchar(1000) default '[]'")
    @Convert(converter = EquipmentChestConverter.class)
    private List<EquipChest> equipmentChest = new ArrayList<>();

    @OneToOne(mappedBy = "master", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Clan clan;

    @OneToOne(mappedBy = "player", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private ClanMember clanMember;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_PLAYER_USER_ID"), referencedColumnName = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "active_character_id", foreignKey = @ForeignKey(name = "FK_PLAYER_ACTIVE_CHARACTER_ID"), referencedColumnName = "player_character_id", nullable = false)
    @JsonIgnore
    private PlayerCharacters activeCharacter;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "approvedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Post> approvedPosts = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PostFollow> follows = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PlayerNotification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ClanApproval> pendingClanApprovals = new ArrayList<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PlayerCharacters> playerCharacters = new ArrayList<>();

    public Player(Long id) {
        this.id = id;
    }
}
