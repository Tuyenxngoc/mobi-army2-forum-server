package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.CreatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Post;

public interface PostService {

    Post createPost(CreatePostRequestDto requestDto);

    Post updatePost(Long id, UpdatePostRequestDto requestDto);

    CommonResponseDto deletePost(Long id);

    Post getPostById(Long id);

    PaginationResponseDto<Post> getPosts(PaginationFullRequestDto requestDto);

}
