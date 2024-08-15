package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.*;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.PostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostDetailForAdminResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostsForAdminResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Category;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.domain.entity.PostFollow;
import com.tuyenngoc.army2forum.domain.mapper.PostMapper;
import com.tuyenngoc.army2forum.domain.specification.PostSpecification;
import com.tuyenngoc.army2forum.exception.BadRequestException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.*;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.PlayerNotificationService;
import com.tuyenngoc.army2forum.service.PostService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import com.tuyenngoc.army2forum.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostServiceImpl implements PostService {

    @Value("${forum.max-pending-posts}")
    byte maxPendingPosts = 10;

    final PostRepository postRepository;

    final PlayerRepository playerRepository;

    final CategoryRepository categoryRepository;

    final PostMapper postMapper;

    final MessageSource messageSource;

    final PostFollowRepository postFollowRepository;

    final LikeRepository likeRepository;

    final PlayerNotificationService playerNotificationService;

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, postId));
    }

    @Override
    public GetPostDetailResponseDto getPostById(Long postId, CustomUserDetails userDetails) {
        Post post = postRepository.findByIdAndIsApprovedTrue(postId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, postId));

        postRepository.incrementViewCount(postId);

        GetPostDetailResponseDto responseDto = new GetPostDetailResponseDto(post);
        if (userDetails != null) {
            boolean userHasLiked = likeRepository.existsByPostIdAndPlayerId(postId, userDetails.getPlayerId());
            responseDto.getLike().setHasLikes(userHasLiked);

            boolean isFollowing = postFollowRepository.existsByPostIdAndPlayerId(postId, userDetails.getPlayerId());
            responseDto.setFollowed(isFollowing);
        }

        return responseDto;
    }

    @Override
    public PaginationResponseDto<GetPostResponseDto> getPosts(PaginationRequestDto requestDto, Long categoryId) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto);

        Page<GetPostResponseDto> page = postRepository.getPosts(pageable, categoryId);
        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, page);

        PaginationResponseDto<GetPostResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    @Transactional
    public CommonResponseDto createPost(PostRequestDto requestDto, CustomUserDetails userDetails) {
        long pendingPostsCount = postRepository.countByPlayerIdAndIsApprovedFalse(userDetails.getPlayerId());
        if (pendingPostsCount >= maxPendingPosts) {
            throw new BadRequestException(ErrorMessage.Post.ERR_MAX_PENDING_POSTS, maxPendingPosts);
        }

        Player player = playerRepository.findById(userDetails.getPlayerId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, userDetails.getPlayerId()));

        Category category = null;
        if (requestDto.getCategoryId() != null) {
            category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId()));
        }

        Post post = postMapper.toPost(requestDto);
        post.setCategory(category);
        post.setPlayer(player);

        String[] requiredRoles = {RoleConstant.ROLE_ADMIN.name(), RoleConstant.ROLE_SUPER_ADMIN.name()};
        boolean hasRequiredRole = SecurityUtils.hasRequiredRole(userDetails, requiredRoles);
        String message;
        if (!hasRequiredRole) {
            post.setPriority(0);
            message = messageSource.getMessage(SuccessMessage.Post.CREATE, null, LocaleContextHolder.getLocale());
        } else {
            post.setApproved(true);
            post.setApprovedBy(player);
            message = messageSource.getMessage(SuccessMessage.Post.ADMIN_CREATE, null, LocaleContextHolder.getLocale());
        }

        postRepository.save(post);

        return new CommonResponseDto(message);
    }

    @Override
    @Transactional
    public CommonResponseDto updatePost(Long postId, PostRequestDto requestDto) {
        Post post = getPostById(postId);

        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        if (requestDto.getCategoryId() != null) {
            boolean isCategoryExists = categoryRepository.existsById(requestDto.getCategoryId());
            if (!isCategoryExists) {
                throw new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, requestDto.getCategoryId());
            }
            post.setCategory(new Category(requestDto.getCategoryId()));
        } else {
            post.setCategory(null);
        }
        if (requestDto.getPriority() != null) {
            post.setPriority(requestDto.getPriority());
        }

        postRepository.save(post);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    @Transactional
    public CommonResponseDto deletePost(Long postId, CustomUserDetails userDetails) {
        Post post = getPostById(postId);

        String[] requiredRoles = {RoleConstant.ROLE_ADMIN.name(), RoleConstant.ROLE_SUPER_ADMIN.name()};
        boolean hasRequiredRole = SecurityUtils.hasRequiredRole(userDetails, requiredRoles);

        if (hasRequiredRole) {
            postRepository.deleteById(postId);
        } else {
            postRepository.deleteByIdAndPlayerId(postId, userDetails.getPlayerId());
        }

        String title = messageSource.getMessage(SuccessMessage.Notification.POST_DELETE, null, LocaleContextHolder.getLocale());
        String notificationMessage = messageSource.getMessage(SuccessMessage.Notification.POST_DELETE_DETAIL, new Object[]{postId, post.getTitle()}, LocaleContextHolder.getLocale());
        playerNotificationService.createNotification(userDetails.getPlayerId(), title, notificationMessage);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    @Transactional
    public CommonResponseDto approvePost(Long postId, Long playerId) {
        Post post = getPostById(postId);
        if (post.isApproved()) {
            String alreadyApprovedMessage = messageSource.getMessage(ErrorMessage.Post.ALREADY_APPROVED, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(alreadyApprovedMessage);
        }

        Player approver = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, playerId));

        post.setApproved(true);
        post.setApprovedBy(approver);
        postRepository.save(post);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CommonConstant.PATTERN_DATE_TIME);
        String formattedDateTime = now.format(formatter);

        String title = messageSource.getMessage(SuccessMessage.Notification.POST_APPROVED, null, LocaleContextHolder.getLocale());
        String message = messageSource.getMessage(SuccessMessage.Notification.POST_APPROVE_DETAIL, new Object[]{post.getId(), post.getTitle(), approver.getUser().getUsername(), formattedDateTime}, LocaleContextHolder.getLocale());
        playerNotificationService.createNotification(post.getPlayer().getId(), title, message);

        String successMessage = messageSource.getMessage(SuccessMessage.Post.APPROVED, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(successMessage);
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

    @Override
    public PaginationResponseDto<GetPostsForAdminResponseDto> getPostsForAdmin(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.POST);

        Page<Post> page = postRepository.findAll(
                PostSpecification.filterPosts(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable
        );

        List<GetPostsForAdminResponseDto> items = page.getContent().stream()
                .map(GetPostsForAdminResponseDto::new)
                .collect(Collectors.toList());

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.POST, page);

        PaginationResponseDto<GetPostsForAdminResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public GetPostDetailForAdminResponseDto getPostByIdForAdmin(Long id) {
        Post post = getPostById(id);

        return new GetPostDetailForAdminResponseDto(post);
    }

    @Override
    public PaginationResponseDto<GetPostResponseDto> getPostsByPlayerId(Long playerId, PaginationRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto);

        Page<Post> page = postRepository.findAll(
                PostSpecification.filterPostsByPlayerId(playerId),
                pageable);

        List<GetPostResponseDto> items = page.getContent().stream()
                .map(GetPostResponseDto::new)
                .collect(Collectors.toList());

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, page);

        PaginationResponseDto<GetPostResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }
}
