package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.CommentRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {

    PlayerRepository playerRepository;

    PostRepository postRepository;

    CommentRepository commentRepository;

    CommentMapper commentMapper;

    MessageSource messageSource;

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Comment.ERR_NOT_FOUND_ID, commentId));
    }

    @Override
    public GetCommentResponseDto createComment(Long playerId, Long postId, CommentRequestDto requestDto) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId));

        Post post = postRepository.findByIdAndIsLockedFalse(postId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, postId));

        Comment comment = commentMapper.toComment(requestDto);
        comment.setPlayer(player);
        comment.setPost(post);

        commentRepository.save(comment);

        return new GetCommentResponseDto(comment);
    }

    @Override
    public GetCommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto) {
        Comment comment = getCommentById(commentId);

        comment.setContent(requestDto.getContent());
        commentRepository.save(comment);

        return new GetCommentResponseDto(comment);
    }

    @Override
    public CommonResponseDto deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<GetCommentResponseDto> getCommentsByPostId(Long postId, PaginationRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto);

        Page<GetCommentResponseDto> page = commentRepository.getByPostId(postId, pageable);
        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, page);

        PaginationResponseDto<GetCommentResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

}
