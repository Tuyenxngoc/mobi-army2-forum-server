package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    void deleteByIdAndPlayerId(Long postId, Long playerId);

    long countByPlayerIdAndIsApprovedFalse(Long playerId);

    Optional<Post> findByIdAndIsLockedFalse(Long postId);

    Optional<Post> findByIdAndIsApprovedTrue(Long postId);

    @Query("SELECT p.isApproved " +
            "FROM Post p " +
            "WHERE p.id = :id")
    Boolean findIsApprovedById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Post p " +
            "SET p.viewCount = p.viewCount + 1 " +
            "WHERE p.id = :postId")
    void incrementViewCount(@Param("postId") Long postId);

    @Query("SELECT new com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto(p) " +
            "FROM Post p LEFT JOIN p.category c " +
            "WHERE p.isApproved = TRUE AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId) " +
            "ORDER BY p.priority DESC, p.createdDate DESC")
    Page<GetPostResponseDto> getPosts(Pageable pageable, @Param("categoryId") Long categoryId);

    @Query("SELECT new com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto(p) " +
            "FROM Post p " +
            "WHERE p.isApproved = FALSE " +
            "ORDER BY p.createdDate ASC")
    Page<GetPostResponseDto> findByApprovedFalse(Pageable pageable);

    @Query("SELECT new com.tuyenngoc.army2forum.domain.dto.response.post.GetPostDetailResponseDto(p) " +
            "FROM Post p " +
            "WHERE p.isApproved = FALSE AND p.id = :postId")
    Optional<GetPostDetailResponseDto> getPostById(@Param("postId") Long postId);

    @Query("SELECT new com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto(p) " +
            "FROM Post p JOIN p.follows f " +
            "WHERE f.player.id = :playerId")
    Page<GetPostResponseDto> findFollowingPostsByPlayerId(
            @Param("playerId") Long playerId,
            Pageable pageable
    );
}