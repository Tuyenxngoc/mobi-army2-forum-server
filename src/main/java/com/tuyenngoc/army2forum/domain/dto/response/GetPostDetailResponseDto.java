package com.tuyenngoc.army2forum.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuyenngoc.army2forum.domain.dto.CategoryDto;
import com.tuyenngoc.army2forum.domain.dto.LikeDto;
import com.tuyenngoc.army2forum.domain.dto.PlayerDto;
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
public class GetPostDetailResponseDto extends UserDateAuditingDto {

    private Long id;

    private String title;

    private String content;

    private int viewCount;

    private boolean isLocked;

    private PlayerDto player;

    private LikeDto like;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PlayerDto approvedBy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CategoryDto category;

    public GetPostDetailResponseDto(Post post) {
        this.setCreatedDate(post.getCreatedDate());
        this.setLastModifiedDate(post.getLastModifiedDate());
        this.setCreatedBy(post.getCreatedBy());
        this.setLastModifiedBy(post.getLastModifiedBy());

        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.isLocked = post.isLocked();
        this.player = new PlayerDto(post.getPlayer());
        this.approvedBy = new PlayerDto(post.getApprovedBy());
        this.like = new LikeDto(post.getLikes());
        if (post.getCategory() != null) {
            this.category = new CategoryDto(post.getCategory());
        }
    }

}
