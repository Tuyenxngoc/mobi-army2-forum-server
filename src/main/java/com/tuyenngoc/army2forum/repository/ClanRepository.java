package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.Clan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClanRepository extends JpaRepository<Clan, Short> {

}
