package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.GiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GiftCodeRepository extends JpaRepository<GiftCode, Long> {

    Optional<GiftCode> findByCode(String code);

}
