package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostIdAndPlayerId(Long postId, Long playerId);

}
