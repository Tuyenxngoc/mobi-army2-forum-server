package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.NewCommentRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateCommentRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetCommentResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Comment;

public interface CommentService {

    Comment getCommentById(Long id);

    GetCommentResponseDto createComment(Long playerId, NewCommentRequestDto requestDto);

    GetCommentResponseDto updateComment(Long id, UpdateCommentRequestDto requestDto);

    CommonResponseDto deleteComment(Long id);

    PaginationResponseDto<GetCommentResponseDto> getCommentsByPostId(Long postId, PaginationRequestDto requestDto);

}
