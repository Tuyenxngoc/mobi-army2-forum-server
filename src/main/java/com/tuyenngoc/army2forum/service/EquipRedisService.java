package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.Equip;

import java.util.List;
import java.util.Optional;

public interface EquipRedisService {

    void addEquip(Equip equipment);

    Optional<Equip> getEquip(Byte characterId, Byte equipType, Short equipIndex);

    List<Equip> getEquips(Byte characterId, Byte equipType);

    int countEquip();

}
