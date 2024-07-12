package com.tuyenngoc.army2forum.domain.mapper;

import com.tuyenngoc.army2forum.domain.dto.request.NewCommentRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toComment(NewCommentRequestDto requestDto);

}
