package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.NewCommentRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateCommentRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetCommentResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Comment;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.domain.mapper.CommentMapper;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.CommentRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.PostRepository;
import com.tuyenngoc.army2forum.service.CommentService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final PlayerRepository playerRepository;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Override
    public GetCommentResponseDto createComment(Long playerId, NewCommentRequestDto requestDto) {
        Comment comment = commentMapper.toComment(requestDto);

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("Player not found"));

        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new NotFoundException("Post not found"));

        comment.setPlayer(player);
        comment.setPost(post);

        commentRepository.save(comment);

        return new GetCommentResponseDto(comment);
    }

    @Override
    public Comment updateComment(Long id, Long playerId, UpdateCommentRequestDto requestDto) {
        Player currentPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException("Player not found"));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        // Kiểm tra quyền của người dùng hiện tại
        boolean isAdmin = currentPlayer.getUser().getRole().getName().contains("ADMIN");
        boolean isOwner = comment.getPlayer().getId().equals(playerId);

        if (isAdmin || isOwner) {
            // Cập nhật comment
            comment.setContent(requestDto.getContent());
            return commentRepository.save(comment);
        } else {
            throw new AccessDeniedException("You do not have permission to update this comment");
        }
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
    public PaginationResponseDto<GetCommentResponseDto> getCommentsByPostId(Long postId, PaginationRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto);

        Page<GetCommentResponseDto> page = commentRepository.getByPostId(pageable);
        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, page);

        PaginationResponseDto<GetCommentResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

}
