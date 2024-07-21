package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.RoleConstant;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.CreatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPostDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Category;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.domain.mapper.PostMapper;
import com.tuyenngoc.army2forum.exception.InvalidException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.CategoryRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.PostRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.PlayerNotificationService;
import com.tuyenngoc.army2forum.service.PostService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import com.tuyenngoc.army2forum.util.SecurityUtils;
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

    private final PlayerNotificationService playerNotificationService;

    @Override
    public Post createPost(CreatePostRequestDto requestDto, Long playerId) {
        long pendingPostsCount = postRepository.countPostPending(playerId);
        if (pendingPostsCount >= 10) {
            throw new InvalidException(ErrorMessage.Post.ERR_MAX_PENDING_POSTS, 10);
        }

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId));

        Category category = null;
        if (requestDto.getCategoryId() != null) {
            category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId()));
        }

        Post post = postMapper.toPost(requestDto);
        post.setCategory(category);
        post.setPlayer(player);

        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Long id, Long playerId, UpdatePostRequestDto requestDto) {
        Post post = postRepository.findByIdAndPlayerId(id, playerId)
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
    public CommonResponseDto deletePost(Long id, CustomUserDetails userDetails) {
        String[] requiredRoles = {RoleConstant.ROLE_ADMIN.name(), RoleConstant.ROLE_SUPER_ADMIN.name()};
        boolean hasRequiredRole = SecurityUtils.hasRequiredRole(userDetails, requiredRoles);

        if (hasRequiredRole) {
            postRepository.deleteById(id);
        } else {
            postRepository.deleteByIdAndPlayerId(id, userDetails.getPlayerId());
        }

        playerNotificationService.createNotification(userDetails.getPlayerId(), "Your post has been deleted");

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public GetPostDetailResponseDto getPostById(Long id, CustomUserDetails userDetails) {
        String[] requiredRoles = {RoleConstant.ROLE_ADMIN.name(), RoleConstant.ROLE_SUPER_ADMIN.name()};
        boolean hasRequiredRole = SecurityUtils.hasRequiredRole(userDetails, requiredRoles);

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, id));

        postRepository.incrementViewCount(id);

        GetPostDetailResponseDto responseDto = new GetPostDetailResponseDto(post);

        if (userDetails != null) {
            boolean userHasLiked = post.getLikes().stream()
                    .anyMatch(like -> like.getPlayer().getId().equals(userDetails.getPlayerId()));
            responseDto.getLike().setHasLikes(userHasLiked);
        }

        if (!hasRequiredRole) {
            responseDto.setApprovedBy(null);
        }

        return responseDto;
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
        if (post.isApproved()) {
            return new CommonResponseDto("Post is already approved");
        }

        Player approver = playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Player not found"));

        post.setApproved(true);
        post.setApprovedBy(approver);
        postRepository.save(post);

        playerNotificationService.createNotification(post.getPlayer().getId(), "Your post has been approved");

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

    @Override
    public PaginationResponseDto<GetPostResponseDto> getPostsForReview(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.POST);

        Page<GetPostResponseDto> page = postRepository.findByApprovedFalse(pageable);
        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.POST, page);

        PaginationResponseDto<GetPostResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

}
