package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.ClanMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClanMemberRepository extends JpaRepository<ClanMember, Long>, JpaSpecificationExecutor<ClanMember> {

    Optional<ClanMember> findByClanIdAndPlayerId(Long clanId, Long playerId);

    Optional<ClanMember> findByIdAndClanId(Long id, Long clanId);

}
