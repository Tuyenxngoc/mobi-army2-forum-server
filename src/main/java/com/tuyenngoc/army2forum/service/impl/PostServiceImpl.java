package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.CreatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Category;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.domain.mapper.PostMapper;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.CategoryRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.PostRepository;
import com.tuyenngoc.army2forum.service.PostService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PlayerRepository playerRepository;

    private final CategoryRepository categoryRepository;

    private final PostMapper postMapper;

    private final MessageSource messageSource;

    @Override
    public Post createPost(CreatePostRequestDto requestDto, Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId()));

        Post post = postMapper.toPost(requestDto);
        post.setCategory(category);
        post.setPlayer(player);

        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Long id, Long playerId, UpdatePostRequestDto requestDto) {
        Post post = postRepository.findByPostIdAndPlayerPlayerId(id, playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, id));

        if (requestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId()));
            post.setCategory(category);
        }

        if (requestDto.getTitle() != null && !requestDto.getTitle().isEmpty()) {
            post.setTitle(requestDto.getTitle());
        }

        if (requestDto.getContent() != null && !requestDto.getContent().isEmpty()) {
            post.setContent(requestDto.getContent());
        }

        return postRepository.save(post);
    }

    @Override
    public CommonResponseDto deletePost(Long id, Long playerId) {
        int result = postRepository.deleteByIdAndPlayerId(id, playerId);
        if (result == 0) {
            throw new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, id);
        }

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public Post getPostById(Long id) {
        postRepository.incrementViewCount(id);
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public PaginationResponseDto<GetPostResponseDto> getPosts(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.POST);

        Page<GetPostResponseDto> page = postRepository.getPosts(pageable);
        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.POST, page);

        PaginationResponseDto<GetPostResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    @Transactional
    public CommonResponseDto approvePost(Long id, Long playerId) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        Player approver = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Player not found"));

        post.setApproved(true);
        post.setApprovedBy(approver);
        postRepository.save(post);

        return new CommonResponseDto("Post approved successfully");
    }

    @Override
    @Transactional
    public CommonResponseDto lockPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        post.setLocked(true);
        postRepository.save(post);

        return new CommonResponseDto("Post locked successfully");
    }

    @Override
    @Transactional
    public CommonResponseDto unlockPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        post.setLocked(false);
        postRepository.save(post);

        return new CommonResponseDto("Post unlocked successfully");
    }

}
