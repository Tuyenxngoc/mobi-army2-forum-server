package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.ClanApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ClanApprovalRepository extends JpaRepository<ClanApproval, Long> {

    boolean existsByClanIdAndPlayerId(Long clanId, Long playerId);

    @Modifying
    @Transactional
    void removeAllByPlayerId(Long player);

    Optional<ClanApproval> findByIdAndClanId(Long id, Long clanId);

}
