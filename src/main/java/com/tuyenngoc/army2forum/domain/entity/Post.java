package com.tuyenngoc.army2forum.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuyenngoc.army2forum.domain.entity.common.UserDateAuditing;
import com.tuyenngoc.army2forum.domain.entity.listener.CategoryListener;
import com.tuyenngoc.army2forum.domain.entity.listener.PostListener;
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
@EntityListeners(PostListener.class)
@Table(name = "posts")
public class Post extends UserDateAuditing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    @Column(name = "priority", nullable = false)
    private int priority = 0;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved = false;

    @Column(name = "is_locked", nullable = false)
    private boolean isLocked = false;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PostFollow> follows = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", foreignKey = @ForeignKey(name = "FK_POST_PLAYER_ID"), referencedColumnName = "player_id", nullable = false)
    @JsonIgnore
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_id", foreignKey = @ForeignKey(name = "FK_POST_APPROVED_BY_ID"), referencedColumnName = "player_id")
    @JsonIgnore
    private Player approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "FK_POST_CATEGORY_ID"), referencedColumnName = "category_id")
    @JsonIgnore
    private Category category;

    public Post(Long id) {
        this.id = id;
    }
}