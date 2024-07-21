package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.RoleConstant;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationSortRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.CreatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPostDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Category;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.domain.entity.PostFollow;
import com.tuyenngoc.army2forum.domain.mapper.PostMapper;
import com.tuyenngoc.army2forum.exception.InvalidException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.CategoryRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.PostFollowRepository;
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

    private static final byte MAX_PENDING_POSTS = 10;

    private final PostRepository postRepository;

    private final PlayerRepository playerRepository;

    private final CategoryRepository categoryRepository;

    private final PostMapper postMapper;

    private final MessageSource messageSource;

    private final PostFollowRepository postFollowRepository;

    private final PlayerNotificationService playerNotificationService;

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, postId));
    }

    @Override
    @Transactional
    public Post createPost(CreatePostRequestDto requestDto, Long playerId) {
        long pendingPostsCount = postRepository.countByPlayerIdAndIsApprovedFalse(playerId);
        if (pendingPostsCount >= MAX_PENDING_POSTS) {
            throw new InvalidException(ErrorMessage.Post.ERR_MAX_PENDING_POSTS, MAX_PENDING_POSTS);
        }

        boolean isPlayerExists = playerRepository.existsById(playerId);
        if (!isPlayerExists) {
            throw new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, playerId);
        }
        Category category = null;
        if (requestDto.getCategoryId() != null) {
            boolean isCategoryExists = categoryRepository.existsById(requestDto.getCategoryId());
            if (!isCategoryExists) {
                throw new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId());
            }

            category = new Category(requestDto.getCategoryId());
        }

        Post post = postMapper.toPost(requestDto);
        post.setCategory(category);
        post.setPlayer(new Player(playerId));

        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post updatePost(Long postId, UpdatePostRequestDto requestDto) {
        Post post = getPostById(postId);

        if (requestDto.getCategoryId() != null) {
            boolean isCategoryExists = categoryRepository.existsById(requestDto.getCategoryId());
            if (!isCategoryExists) {
                throw new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId());
            }
            post.setCategory(new Category(requestDto.getCategoryId()));
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
    @Transactional
    public CommonResponseDto deletePost(Long postId, CustomUserDetails userDetails) {
        String[] requiredRoles = {RoleConstant.ROLE_ADMIN.name(), RoleConstant.ROLE_SUPER_ADMIN.name()};
        boolean hasRequiredRole = SecurityUtils.hasRequiredRole(userDetails, requiredRoles);

        if (hasRequiredRole) {
            postRepository.deleteById(postId);
        } else {
            postRepository.deleteByIdAndPlayerId(postId, userDetails.getPlayerId());
        }

        String notificationMessage = messageSource.getMessage(SuccessMessage.Notification.POST_DELETE, null, LocaleContextHolder.getLocale());
        playerNotificationService.createNotification(userDetails.getPlayerId(), notificationMessage);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public GetPostDetailResponseDto getPostById(Long postId, CustomUserDetails userDetails) {
        String[] requiredRoles = {RoleConstant.ROLE_ADMIN.name(), RoleConstant.ROLE_SUPER_ADMIN.name()};
        boolean hasRequiredRole = SecurityUtils.hasRequiredRole(userDetails, requiredRoles);

        Post post = getPostById(postId);
        postRepository.incrementViewCount(postId);
        GetPostDetailResponseDto responseDto = new GetPostDetailResponseDto(post);

        if (userDetails != null) {
            boolean userHasLiked = post.getLikes().stream()
                    .anyMatch(like -> like.getPlayer().getId().equals(userDetails.getPlayerId()));
            responseDto.getLike().setHasLikes(userHasLiked);

            boolean isFollowing = postFollowRepository.existsByPostIdAndPlayerId(postId, userDetails.getPlayerId());
            responseDto.setFollowed(isFollowing);
        }

        if (!hasRequiredRole) {
            responseDto.setApprovedBy(null);
        }

        return responseDto;
    }

    @Override
    public PaginationResponseDto<GetPostResponseDto> getPosts(PaginationSortRequestDto requestDto) {
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
    public CommonResponseDto approvePost(Long postId, Long playerId) {
        Post post = getPostById(postId);
        if (post.isApproved()) {
            String alreadyApprovedMessage = messageSource.getMessage(ErrorMessage.Post.ALREADY_APPROVED, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(alreadyApprovedMessage);
        }

        boolean approver = playerRepository.existsById(playerId);
        if (!approver) {
            throw new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId);
        }

        post.setApproved(true);
        post.setApprovedBy(new Player(playerId));
        postRepository.save(post);

        String notificationMessage = messageSource.getMessage(SuccessMessage.Notification.POST_APPROVED, null, LocaleContextHolder.getLocale());
        playerNotificationService.createNotification(post.getPlayer().getId(), notificationMessage);

        String successMessage = messageSource.getMessage(SuccessMessage.Post.APPROVED, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(successMessage);
    }

    @Override
    public PaginationResponseDto<GetPostResponseDto> getPostsForReview(PaginationSortRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.POST);

        Page<GetPostResponseDto> page = postRepository.findByApprovedFalse(pageable);
        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.POST, page);

        PaginationResponseDto<GetPostResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    @Transactional
    public CommonResponseDto toggleLockPost(Long postId) {
        Post post = getPostById(postId);

        post.setLocked(!post.isLocked());
        postRepository.save(post);

        String messageKey = post.isLocked() ? SuccessMessage.Post.LOCKED : SuccessMessage.Post.UNLOCKED;
        String message = messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());

        return new CommonResponseDto(message);
    }

    @Override
    @Transactional
    public CommonResponseDto toggleFollowPost(Long postId, Long playerId) {
        boolean isFollowing = postFollowRepository.existsByPostIdAndPlayerId(postId, playerId);

        if (!isFollowing) {
            boolean playerExists = playerRepository.existsById(playerId);
            if (!playerExists) {
                throw new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId);
            }

            boolean postExists = postRepository.existsById(postId);
            if (!postExists) {
                throw new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, postId);
            }

            PostFollow postFollow = new PostFollow();
            postFollow.setPost(new Post(postId));
            postFollow.setPlayer(new Player(playerId));
            postFollowRepository.save(postFollow);

            String followSuccessMessage = messageSource.getMessage(SuccessMessage.Post.FOLLOWED, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(followSuccessMessage);
        } else {
            postFollowRepository.deleteByPostIdAndPlayerId(postId, playerId);

            String unfollowSuccessMessage = messageSource.getMessage(SuccessMessage.Post.UNFOLLOWED, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(unfollowSuccessMessage);
        }
    }
}
