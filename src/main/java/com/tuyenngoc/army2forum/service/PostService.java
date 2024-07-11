package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Post;

public interface PostService {

    Post createPost(Post post);

    Post updatePost(Long id, Post post);

    void deletePost(Long id);

    Post getPostById(Long id);

    PaginationResponseDto<Post> getPosts(PaginationFullRequestDto requestDto);

}
