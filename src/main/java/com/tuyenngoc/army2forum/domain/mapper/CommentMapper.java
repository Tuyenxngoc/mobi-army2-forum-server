package com.tuyenngoc.army2forum.domain.mapper;

import com.tuyenngoc.army2forum.domain.dto.request.CommentRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toComment(CommentRequestDto requestDto);

}
