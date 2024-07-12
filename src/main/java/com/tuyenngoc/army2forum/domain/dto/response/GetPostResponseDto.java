package com.tuyenngoc.army2forum.domain.dto.response;

import com.tuyenngoc.army2forum.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostResponseDto {

    private Long id;

    private String title;

    private int comments;

    private int favorites;

    private long views;

    private String author;

    public GetPostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.comments = post.getComments().size();
        this.favorites = post.getLikes().size();
        this.views = post.getViewCount();
        this.author = post.getPlayer().getUser().getUsername();
    }

}
