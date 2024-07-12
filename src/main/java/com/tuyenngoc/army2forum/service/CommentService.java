package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.request.NewCommentRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment(Long playerId, NewCommentRequestDto requestDto);

    Comment updateComment(Long id, Comment comment);

    void deleteComment(Long id);

    Comment getCommentById(Long id);

    List<Comment> getAllComments();

    List<Comment> getCommentsByPostId(Long postId);

}
