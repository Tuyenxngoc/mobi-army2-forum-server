package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.domain.entity.Like;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Post;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.LikeRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.PostRepository;
import com.tuyenngoc.army2forum.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    private final PostRepository postRepository;

    private final PlayerRepository playerRepository;

    @Override
    public Like toggleLike(Long postId, Long playerId) {
        Optional<Like> like = likeRepository.findByPostIdAndPlayerId(postId, playerId);

        if (like.isPresent()) {
            likeRepository.delete(like.get());
            return null;
        } else {
            Player player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId));

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Post.ERR_NOT_FOUND_ID, postId));

            Like newLike = new Like();
            newLike.setPost(post);
            newLike.setPlayer(player);
            return likeRepository.save(newLike);
        }
    }
}
