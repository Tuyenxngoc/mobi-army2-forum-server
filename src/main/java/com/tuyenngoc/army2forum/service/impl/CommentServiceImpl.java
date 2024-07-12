package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.dto.request.NewCommentRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Comment;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.domain.mapper.CommentMapper;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.CommentRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.PostRepository;
import com.tuyenngoc.army2forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final PlayerRepository playerRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Override
    public Comment createComment(Long playerId, NewCommentRequestDto requestDto) {
        Comment comment = commentMapper.toComment(requestDto);

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("Player not found"));

        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found"));

        comment.setPlayer(player);
        comment.setPost(post);

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

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

}
