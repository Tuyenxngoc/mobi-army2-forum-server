package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.Post;

import java.util.List;

public interface PostService {

    Post createPost(Post post);

    Post updatePost(Long id, Post post);

    void deletePost(Long id);

    Post getPostById(Long id);

    List<Post> getAllPosts();

}
