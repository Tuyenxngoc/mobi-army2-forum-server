package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

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

}
