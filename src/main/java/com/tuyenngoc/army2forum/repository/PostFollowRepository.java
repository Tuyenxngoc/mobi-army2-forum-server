package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.PostFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface PostFollowRepository extends JpaRepository<PostFollow, Long> {

    boolean existsByPostIdAndPlayerId(long postId, long playerId);

    @Modifying
    void deleteByPostIdAndPlayerId(long postId, long playerId);
}
