package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.PostRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@Tag(name = "Post")
public class PostController {

    private final PostService postService;

    @Operation(summary = "API get posts")
    @GetMapping(UrlConstant.Post.GET_ALL)
    public ResponseEntity<?> getPost(
            @ParameterObject PaginationRequestDto requestDto,
            @RequestParam(required = false) Long categoryId
    ) {
        return VsResponseUtil.success(postService.getPosts(requestDto, categoryId));
    }

    @Operation(summary = "API get post by id")
    @GetMapping(UrlConstant.Post.GET_BY_ID)
    public ResponseEntity<?> getPostById(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(postService.getPostById(id, userDetails));
    }

    @Operation(summary = "API create a new post")
    @PostMapping(UrlConstant.Post.CREATE)
    public ResponseEntity<?> createPost(
            @Valid @RequestBody PostRequestDto postDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(postService.createPost(postDto, userDetails));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MODERATOR')")
    @Operation(summary = "API update an existing post")
    @PutMapping(UrlConstant.Post.UPDATE)
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDto requestDto
    ) {
        return VsResponseUtil.success(postService.updatePost(id, requestDto));
    }

    @Operation(summary = "API delete a post")
    @DeleteMapping(UrlConstant.Post.DELETE)
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(postService.deletePost(id, userDetails));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MODERATOR')")
    @Operation(summary = "API approve a post")
    @PostMapping(UrlConstant.Post.APPROVE)
    public ResponseEntity<?> approvePost(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(postService.approvePost(id, userDetails.getPlayerId()));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'MODERATOR')")
    @Operation(summary = "API toggle lock a post")
    @PostMapping(UrlConstant.Post.LOCK)
    public ResponseEntity<?> toggleLockPost(@PathVariable Long id) {
        return VsResponseUtil.success(postService.toggleLockPost(id));
    }

    @Operation(summary = "API toggle follow a post")
    @PostMapping(UrlConstant.Post.FOLLOW)
    public ResponseEntity<?> toggleFollowPost(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(postService.toggleFollowPost(id, userDetails.getPlayerId()));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API get posts for admin")
    @GetMapping(UrlConstant.Post.ADMIN_GET_ALL)
    public ResponseEntity<?> getPostsForAdmin(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(postService.getPostsForAdmin(requestDto));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API get post by id for admin")
    @GetMapping(UrlConstant.Post.ADMIN_GET_BY_ID)
    public ResponseEntity<?> getPostByIdForAdmin(@PathVariable Long id) {
        return VsResponseUtil.success(postService.getPostByIdForAdmin(id));
    }

    @Operation(summary = "API get posts by user")
    @GetMapping(UrlConstant.Post.GET_BY_PLAYER_ID)
    public ResponseEntity<?> getPostsByPlayerId(
            @ParameterObject PaginationRequestDto requestDto,
            @PathVariable Long playerId
    ) {
        return VsResponseUtil.success(postService.getPostsByPlayerId(playerId, requestDto));
    }

}
