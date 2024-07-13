package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.dto.response.GetCommentResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    @Query("SELECT new com.tuyenngoc.army2forum.domain.dto.response.GetCommentResponseDto(c) " +
            "FROM Comment c " +
            "ORDER BY c.createdDate ASC")
    Page<GetCommentResponseDto> getByPostId(Pageable pageable);
}
