package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {

    @Query("SELECT p.id FROM Player p JOIN p.user u JOIN u.role r WHERE r.name IN :roles")
    List<Long> findPlayerIdsByRoleNames(@Param("roles") List<String> roles);

    @Query("SELECT p.id FROM Player p")
    List<Long> findAllPlayerIds();

}
