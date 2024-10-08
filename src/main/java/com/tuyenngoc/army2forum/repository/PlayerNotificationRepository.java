package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.dto.response.GetPlayerNotificationResponseDto;
import com.tuyenngoc.army2forum.domain.entity.PlayerNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerNotificationRepository extends JpaRepository<PlayerNotification, Long> {

    @Query("SELECT new com.tuyenngoc.army2forum.domain.dto.response.GetPlayerNotificationResponseDto(p) " +
            "FROM PlayerNotification p WHERE " +
            "p.player.id = :playerId " +
            "ORDER BY p.createdDate DESC")
    Page<GetPlayerNotificationResponseDto> findByPlayerId(
            @Param("playerId") Long playerId,
            Pageable pageable
    );

    @Query("SELECT p " +
            "FROM PlayerNotification p WHERE " +
            "p.id = :id AND " +
            "p.player.id = :playerId")
    Optional<PlayerNotification> findByIdAndPlayerId(
            @Param("id") Long id,
            @Param("playerId") Long playerId
    );

    @Modifying
    void deleteByIdAndPlayerId(Long id, Long playerId);
}
