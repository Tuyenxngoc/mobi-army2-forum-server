package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
}
