package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.PlayerGiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerGiftCodeRepository extends JpaRepository<PlayerGiftCode, Long>, JpaSpecificationExecutor<PlayerGiftCode> {

}
