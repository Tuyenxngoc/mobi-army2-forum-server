package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.Like;

import java.util.List;

public interface LikeService {

    Like createLike(Like like);

    Like updateLike(Long id, Like like);

    void deleteLike(Long id);

    Like getLikeById(Long id);

    List<Like> getAllLikes();

}
