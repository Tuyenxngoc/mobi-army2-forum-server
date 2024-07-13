package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.NewCommentRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateCommentRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetCommentResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Comment;

import java.util.List;

public interface CommentService {

    GetCommentResponseDto createComment(Long playerId, NewCommentRequestDto requestDto);

    Comment updateComment(Long id, Long playerId, UpdateCommentRequestDto requestDto);

    void deleteComment(Long id);

    Comment getCommentById(Long id);

    List<Comment> getAllComments();

    PaginationResponseDto<GetCommentResponseDto> getCommentsByPostId(Long postId, PaginationRequestDto requestDto);

}
