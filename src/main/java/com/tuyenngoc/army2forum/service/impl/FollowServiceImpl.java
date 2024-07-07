package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.entity.Follow;
import com.tuyenngoc.army2forum.repository.FollowRepository;
import com.tuyenngoc.army2forum.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    @Override
    public Follow createFollow(Follow follow) {
        return followRepository.save(follow);
    }

    @Override
    public Follow updateFollow(Long id, Follow follow) {
        Optional<Follow> existingFollow = followRepository.findById(id);
        if (existingFollow.isPresent()) {
            Follow updatedFollow = existingFollow.get();
            updatedFollow.setFollower(follow.getFollower());
            updatedFollow.setFollowed(follow.getFollowed());
            return followRepository.save(updatedFollow);
        }
        return null;
    }

    @Override
    public void deleteFollow(Long id) {
        followRepository.deleteById(id);
    }

    @Override
    public Follow getFollowById(Long id) {
        return followRepository.findById(id).orElse(null);
    }

    @Override
    public List<Follow> getAllFollows() {
        return followRepository.findAll();
    }
}
