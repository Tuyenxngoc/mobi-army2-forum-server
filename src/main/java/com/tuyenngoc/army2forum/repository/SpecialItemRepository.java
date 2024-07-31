package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.SpecialItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialItemRepository extends JpaRepository<SpecialItem, Long> {
}
