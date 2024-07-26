package com.tuyenngoc.army2forum.domain.dto.response.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuyenngoc.army2forum.domain.dto.CategoryDto;
import com.tuyenngoc.army2forum.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostDetailForAdminResponseDto {

    private long id;

    private String title;

    private String content;

    private int priority;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CategoryDto category;

    public GetPostDetailForAdminResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.priority = post.getPriority();
        if (post.getCategory() != null) {
            this.category = new CategoryDto(post.getCategory());
        }
    }

}
