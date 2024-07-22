package com.tuyenngoc.army2forum.domain.dto.response;

import com.tuyenngoc.army2forum.domain.dto.common.UserDateAuditingDto;
import com.tuyenngoc.army2forum.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostResponseDto extends UserDateAuditingDto {

    private Long id;

    private String title;

    private int comments;

    private int favorites;

    private long views;

    private boolean locked;

    private int priority;

    private String author;

    public GetPostResponseDto(Post post) {
        this.setCreatedDate(post.getCreatedDate());
        this.setLastModifiedDate(post.getLastModifiedDate());
        this.setCreatedBy(post.getCreatedBy());
        this.setLastModifiedBy(post.getLastModifiedBy());

        this.id = post.getId();
        this.title = post.getTitle();
        this.comments = post.getComments().size();
        this.favorites = post.getLikes().size();
        this.views = post.getViewCount();
        this.locked = post.isLocked();
        this.priority = post.getPriority();
        this.author = post.getPlayer().getUser().getUsername();
    }

}
