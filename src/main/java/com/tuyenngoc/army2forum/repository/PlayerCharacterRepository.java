package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.dto.response.player.GetCharacterResponseDto;
import com.tuyenngoc.army2forum.domain.entity.PlayerCharacters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacters, Long> {

    Optional<PlayerCharacters> findByIdAndPlayerId(Long id, Long playerId);

    @Query("SELECT new com.tuyenngoc.army2forum.domain.dto.response.player.GetCharacterResponseDto(pc.id, pc.character.name) " +
            "FROM PlayerCharacters pc " +
            "WHERE pc.player.id = :playerId " +
            "ORDER BY pc.character.id")
    List<GetCharacterResponseDto> getByPlayerId(@Param("playerId") Long playerId);

}
