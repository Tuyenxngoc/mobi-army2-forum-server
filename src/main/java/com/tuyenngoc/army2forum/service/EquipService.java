package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.Equip;

import java.util.List;

public interface EquipService {

    void initCacheEquips();

    List<Equip> getEquipsByCharacterIdAndType(byte characterId, byte type);

}
