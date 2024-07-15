package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.dto.response.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndPlayerId(Long postId, Long playerId);

    @Modifying
    @Transactional
    @Query("DELETE " +
            "FROM Post p WHERE " +
            "p.id = :id AND " +
            "p.player.id = :playerId")
    int deleteByIdAndPlayerId(@Param("id") Long id, @Param("playerId") Long playerId);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void incrementViewCount(@Param("postId") Long postId);

    @Query("SELECT new com.tuyenngoc.army2forum.domain.dto.response.GetPostResponseDto(p) " +
            "FROM Post p " +
            "WHERE p.isApproved = TRUE " +
            "ORDER BY p.createdDate DESC")
    Page<GetPostResponseDto> getPosts(Pageable pageable);

    @Query("SELECT new com.tuyenngoc.army2forum.domain.dto.response.GetPostResponseDto(p) " +
            "FROM Post p " +
            "WHERE p.isApproved = FALSE " +
            "ORDER BY p.createdDate DESC")
    Page<GetPostResponseDto> findByApprovedFalse(Pageable pageable);

}
