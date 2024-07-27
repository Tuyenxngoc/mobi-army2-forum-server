package com.tuyenngoc.army2forum.domain.mapper;

import com.tuyenngoc.army2forum.domain.dto.request.PostRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost(PostRequestDto requestDto);

}
