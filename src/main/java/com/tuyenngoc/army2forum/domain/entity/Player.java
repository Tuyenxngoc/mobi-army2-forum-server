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

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "players",
        uniqueConstraints = @UniqueConstraint(name = "UN_PLAYER_USER_ID", columnNames = "user_id"))
public class Player extends DateAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long id;

    private int xu;

    private int luong;

    @Column(name = "ruong_item", columnDefinition = "varchar(1000) default '[]'")
    @Convert(converter = ItemChestConverter.class)
    private List<SpecialItemChest> itemChest;

    @Column(name = "ruong_trang_bi", columnDefinition = "varchar(1000) default '[]'")
    @Convert(converter = EquipmentChestConverter.class)
    private List<EquipChest> equipmentChest;

    private boolean online;

    @Column(name = "nv_used", columnDefinition = "tinyint default 0")
    private byte nvUsed;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_PLAYER_USER_ID"), referencedColumnName = "user_id", nullable = false)
    @JsonIgnore
    private User user;

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

    public Player(Long id) {
        this.id = id;
    }
}
