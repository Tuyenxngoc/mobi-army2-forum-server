package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.CreatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePostRequestDto;
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
    public ResponseEntity<?> getPost(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(postService.getPosts(requestDto));
    }

    @Operation(summary = "API get post by id")
    @GetMapping(UrlConstant.Post.GET_BY_ID)
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return VsResponseUtil.success(postService.getPostById(id));
    }

    @Operation(summary = "API create a new post")
    @PostMapping(UrlConstant.Post.CREATE)
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequestDto postDto) {
        return VsResponseUtil.success(postService.createPost(postDto));
    }

    @Operation(summary = "API update an existing post")
    @PutMapping(UrlConstant.Post.UPDATE)
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequestDto requestDto
    ) {
        return VsResponseUtil.success(postService.updatePost(id, requestDto));
    }

    @Operation(summary = "API delete a post")
    @DeleteMapping(UrlConstant.Post.DELETE)
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        return VsResponseUtil.success(postService.deletePost(id));
    }

}
