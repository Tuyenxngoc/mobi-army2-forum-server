package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.Follow;

import java.util.List;

public interface FollowService {

    Follow createFollow(Follow follow);

    Follow updateFollow(Long id, Follow follow);

    void deleteFollow(Long id);

    Follow getFollowById(Long id);

    List<Follow> getAllFollows();

}
