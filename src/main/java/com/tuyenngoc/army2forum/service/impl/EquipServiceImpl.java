package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.entity.Equip;
import com.tuyenngoc.army2forum.repository.EquipRepository;
import com.tuyenngoc.army2forum.service.EquipRedisService;
import com.tuyenngoc.army2forum.service.EquipService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EquipServiceImpl implements EquipService {

    EquipRepository equipRepository;

    EquipRedisService equipRedisService;

    @Override
    public void initCacheEquips() {
        if (equipRedisService.countEquip() != equipRepository.count()) {
            List<Equip> equips = equipRepository.findAll();

            for (Equip equip : equips) {
                equipRedisService.addEquip(equip);
            }
            log.info("Initialized cache for {} equipments.", equips.size());
        } else {
            log.info("Equipment cache is already up to date.");
        }
    }

}
