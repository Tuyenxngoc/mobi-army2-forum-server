package com.tuyenngoc.army2forum.domain.mapper;

import com.tuyenngoc.army2forum.domain.dto.request.post.CreatePostRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost(CreatePostRequestDto requestDto);

}
