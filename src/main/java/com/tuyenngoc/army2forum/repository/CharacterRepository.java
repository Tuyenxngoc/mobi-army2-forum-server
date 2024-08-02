package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Byte> {

    Character findByName(String name);

}
