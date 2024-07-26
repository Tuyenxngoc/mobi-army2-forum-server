package com.tuyenngoc.army2forum.domain.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuyenngoc.army2forum.domain.dto.CategoryDto;
import com.tuyenngoc.army2forum.domain.dto.LikeDto;
import com.tuyenngoc.army2forum.domain.dto.PlayerDto;
import com.tuyenngoc.army2forum.domain.dto.common.DateAuditingDto;
import com.tuyenngoc.army2forum.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostDetailResponseDto extends DateAuditingDto {

    private long id;

    private String title;

    private String content;

    private boolean isLocked;

    private boolean followed;

    private PlayerDto player;

    private LikeDto like;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CategoryDto category;

    public GetPostDetailResponseDto(Post post) {
        this.setCreatedDate(post.getCreatedDate());
        this.setLastModifiedDate(post.getLastModifiedDate());

        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isLocked = post.isLocked();
        this.player = new PlayerDto(post.getPlayer());
        this.like = new LikeDto(post.getLikes());
        if (post.getCategory() != null) {
            this.category = new CategoryDto(post.getCategory());
        }
    }

}
