package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment createComment(Comment comment);

    Comment updateComment(Long id, Comment comment);

    void deleteComment(Long id);

    Comment getCommentById(Long id);

    List<Comment> getAllComments();

}
