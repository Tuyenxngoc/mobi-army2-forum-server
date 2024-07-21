package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.CreatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPostDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

public interface PostService {

    Post createPost(CreatePostRequestDto requestDto, Long playerId);

    Post updatePost(Long id, Long playerId, UpdatePostRequestDto requestDto);

    CommonResponseDto deletePost(Long id, CustomUserDetails playerId);

    GetPostDetailResponseDto getPostById(Long id, CustomUserDetails userDetails);

    PaginationResponseDto<GetPostResponseDto> getPosts(PaginationFullRequestDto requestDto);

    CommonResponseDto approvePost(Long id, Long playerId);

    CommonResponseDto lockPost(Long id);

    CommonResponseDto unlockPost(Long id);

    PaginationResponseDto<GetPostResponseDto> getPostsForReview(PaginationFullRequestDto requestDto);

}
