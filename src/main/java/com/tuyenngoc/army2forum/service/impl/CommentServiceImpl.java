package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.entity.Comment;
import com.tuyenngoc.army2forum.repository.CommentRepository;
import com.tuyenngoc.army2forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long id, Comment comment) {
        Optional<Comment> existingComment = commentRepository.findById(id);
        if (existingComment.isPresent()) {
            Comment updatedComment = existingComment.get();
            updatedComment.setContent(comment.getContent());
            updatedComment.setPlayer(comment.getPlayer());
            updatedComment.setPost(comment.getPost());
            return commentRepository.save(updatedComment);
        }
        return null;
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}
