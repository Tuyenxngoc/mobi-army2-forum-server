package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.PostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostDetailForAdminResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostsForAdminResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

public interface PostService {

    Post getPostById(Long postId);

    GetPostDetailResponseDto getPostById(Long postId, CustomUserDetails userDetails);

    PaginationResponseDto<GetPostResponseDto> getPosts(PaginationRequestDto requestDto, Long categoryId);

    CommonResponseDto createPost(PostRequestDto requestDto, CustomUserDetails userDetails);

    CommonResponseDto updatePost(Long postId, PostRequestDto requestDto);

    CommonResponseDto deletePost(Long postId, CustomUserDetails playerId);

    CommonResponseDto approvePost(Long postId, Long playerId);

    CommonResponseDto toggleLockPost(Long postId);

    CommonResponseDto toggleFollowPost(Long postId, Long playerId);

    PaginationResponseDto<GetPostsForAdminResponseDto> getPostsForAdmin(PaginationFullRequestDto requestDto);

    GetPostDetailForAdminResponseDto getPostByIdForAdmin(Long id);

    PaginationResponseDto<GetPostResponseDto> getPostsByPlayerId(Long playerId, PaginationRequestDto requestDto);

}
