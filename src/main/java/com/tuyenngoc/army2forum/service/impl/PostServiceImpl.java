package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.CreatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePostRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.repository.PostRepository;
import com.tuyenngoc.army2forum.service.PostService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post createPost(CreatePostRequestDto requestDto) {
        return null;
    }

    @Override
    public Post updatePost(Long id, UpdatePostRequestDto requestDto) {
        return null;
    }

    @Override
    public CommonResponseDto deletePost(Long id) {
        return null;
    }

    @Override
    public Post getPostById(Long id) {
        return null;
    }

    @Override
    public PaginationResponseDto<Post> getPosts(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.POST);

        Page<Post> page = postRepository.findAll(pageable);
        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.POST, page);

        PaginationResponseDto<Post> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

}
