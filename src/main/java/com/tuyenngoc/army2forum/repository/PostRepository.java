package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.Post;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByPostIdAndPlayerPlayerId(Long postId, Long playerId);

    @Modifying
    @Transactional
    @Query("DELETE " +
            "FROM Post p WHERE " +
            "p.postId = :id AND " +
            "p.player.playerId = :playerId")
    int deleteByIdAndPlayerId(@Param("id") Long id, @Param("playerId") Long playerId);
}
