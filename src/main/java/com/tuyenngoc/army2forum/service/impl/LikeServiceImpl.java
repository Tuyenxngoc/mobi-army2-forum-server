package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.entity.Like;
import com.tuyenngoc.army2forum.repository.LikeRepository;
import com.tuyenngoc.army2forum.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public Like createLike(Like like) {
        return likeRepository.save(like);
    }

    @Override
    public Like updateLike(Long id, Like like) {
        Optional<Like> existingLike = likeRepository.findById(id);
        if (existingLike.isPresent()) {
            Like updatedLike = existingLike.get();
            updatedLike.setPlayer(like.getPlayer());
            updatedLike.setPost(like.getPost());
            return likeRepository.save(updatedLike);
        }
        return null;
    }

    @Override
    public void deleteLike(Long id) {
        likeRepository.deleteById(id);
    }

    @Override
    public Like getLikeById(Long id) {
        return likeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }
}
