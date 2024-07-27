package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.CommentRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetCommentResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Comment;

public interface CommentService {

    Comment getCommentById(Long commentId);

    GetCommentResponseDto createComment(Long playerId, Long postId, CommentRequestDto requestDto);

    GetCommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto);

    CommonResponseDto deleteComment(Long commentId);

    PaginationResponseDto<GetCommentResponseDto> getCommentsByPostId(Long postId, PaginationRequestDto requestDto);

}
