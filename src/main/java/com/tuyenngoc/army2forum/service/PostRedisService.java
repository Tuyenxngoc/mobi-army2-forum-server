package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto;
import org.springframework.data.domain.Pageable;

public interface PostRedisService {

    void clear();

    void savePost(Long categoryId, Pageable pageable, PaginationResponseDto<GetPostResponseDto> responseDto);

    PaginationResponseDto<GetPostResponseDto> getPostsByCategoryId(Long categoryId, Pageable pageable);

    void deletePost(Long postId);

    void savePost(GetPostDetailResponseDto post);

    GetPostDetailResponseDto getPostById(Long postId);

}
