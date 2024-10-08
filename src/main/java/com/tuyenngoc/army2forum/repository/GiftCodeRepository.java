package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.GiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GiftCodeRepository extends JpaRepository<GiftCode, Long>, JpaSpecificationExecutor<GiftCode> {

    Optional<GiftCode> findByCode(String code);

    boolean existsByCode(String code);

}
