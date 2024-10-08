package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.Equip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipRepository extends JpaRepository<Equip, Short> {

    @Query("SELECT e FROM " +
            "Equip e WHERE " +
            "e.characterId = :characterId AND " +
            "e.equipType = :equipType AND " +
            "e.equipIndex = :equipIndex")
    Optional<Equip> getEquip(
            @Param("characterId") byte characterId,
            @Param("equipType") byte equipType,
            @Param("equipIndex") short equipIndex
    );

    @Query("SELECT e FROM " +
            "Equip e WHERE " +
            "e.characterId = :characterId AND " +
            "e.isDefault = TRUE")
    List<Equip> getEquipDefault(@Param("characterId") byte characterId);

    @Query("SELECT e FROM " +
            "Equip e WHERE " +
            "e.characterId = :characterId AND " +
            "e.equipIndex IN :equippedIndexes")
    List<Equip> getEquipByIndexes(
            @Param("characterId") byte characterId,
            @Param("equippedIndexes") List<Integer> equippedIndexes
    );

}
