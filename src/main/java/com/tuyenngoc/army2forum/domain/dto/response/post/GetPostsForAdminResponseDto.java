package com.tuyenngoc.army2forum.domain.dto.response.post;

import com.tuyenngoc.army2forum.domain.dto.CategoryDto;
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
public class GetPostsForAdminResponseDto extends UserDateAuditingDto {

    private Long id;

    private String title;

    private int comments;

    private int favorites;

    private long followers;

    private long views;

    private boolean locked;

    private boolean approved;

    private int priority;

    private CategoryDto category;

    public GetPostsForAdminResponseDto(Post post) {
        this.setCreatedDate(post.getCreatedDate());
        this.setLastModifiedDate(post.getLastModifiedDate());
        this.setCreatedBy(post.getCreatedBy());
        this.setLastModifiedBy(post.getLastModifiedBy());

        this.id = post.getId();
        this.title = post.getTitle();
        this.comments = post.getComments().size();
        this.favorites = post.getLikes().size();
        this.followers = post.getFollows().size();
        this.views = post.getViewCount();
        this.locked = post.isLocked();
        this.approved = post.isApproved();
        this.priority = post.getPriority();
        if (post.getCategory() != null) {
            this.category = new CategoryDto(post.getCategory());
        }
    }

}
