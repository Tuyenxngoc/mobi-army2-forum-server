package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationSortRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.CreatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPostDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

public interface PostService {

    Post getPostById(Long postId);

    Post createPost(CreatePostRequestDto requestDto, Long playerId);

    Post updatePost(Long postId, UpdatePostRequestDto requestDto);

    CommonResponseDto deletePost(Long postId, CustomUserDetails playerId);

    GetPostDetailResponseDto getPostById(Long postId, CustomUserDetails userDetails);

    PaginationResponseDto<GetPostResponseDto> getPosts(PaginationSortRequestDto requestDto);

    CommonResponseDto approvePost(Long postId, Long playerId);

    PaginationResponseDto<GetPostResponseDto> getPostsForReview(PaginationSortRequestDto requestDto);

    CommonResponseDto toggleLockPost(Long postId);

    CommonResponseDto toggleFollowPost(Long postId, Long playerId);

}
