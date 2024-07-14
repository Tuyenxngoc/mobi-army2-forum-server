package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.Like;

public interface LikeService {

    Like toggleLike(Long postId, Long playerId);
}
