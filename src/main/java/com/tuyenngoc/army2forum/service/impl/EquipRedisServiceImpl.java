package com.tuyenngoc.army2forum.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuyenngoc.army2forum.domain.entity.Equip;
import com.tuyenngoc.army2forum.service.EquipRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EquipRedisServiceImpl implements EquipRedisService {

    RedisTemplate<String, String> redisTemplate;

    ObjectMapper objectMapper;

    private String getKeyFrom(Byte characterId, Byte equipType, Short equipIndex) {
        return String.format("EQUIP_%d_%d_%d", characterId, equipType, equipIndex).toUpperCase();
    }

    @Override
    public void addEquip(Equip equipment) {
        try {
            String key = getKeyFrom(equipment.getCharacterId(), equipment.getEquipType(), equipment.getEquipIndex());
            String json = objectMapper.writeValueAsString(equipment);
            redisTemplate.opsForValue().set(key, json);
            log.info("Saved equip with name: {} to Redis", equipment.getName());
        } catch (Exception e) {
            log.error("Error saving equip to Redis: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<Equip> getEquip(Byte characterId, Byte equipType, Short equipIndex) {
        try {
            String key = getKeyFrom(characterId, equipType, equipIndex);
            String json = redisTemplate.opsForValue().get(key);
            if (StringUtils.hasText(json)) {
                Equip equip = objectMapper.readValue(json, Equip.class);
                return Optional.of(equip);
            } else {
                log.warn("No equipment found in Redis for key: {}", key);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Error retrieving equipment from Redis: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<Equip> getEquips(Byte characterId, Byte equipType) {
        try {
            String keyPattern = String.format("EQUIP_%d_%d_*", characterId, equipType).toUpperCase();
            Set<String> keys = redisTemplate.keys(keyPattern);
            if (keys == null || keys.isEmpty()) {
                log.warn("No equipments found in Redis for characterId: {} and equipType: {}", characterId, equipType);
                return List.of();
            }

            List<Equip> equips = new ArrayList<>();
            for (String key : keys) {
                String json = redisTemplate.opsForValue().get(key);
                if (StringUtils.hasText(json)) {
                    Equip equip = objectMapper.readValue(json, Equip.class);
                    equips.add(equip);
                }
            }

            log.info("Found {} equipments for characterId: {} and equipType: {}", equips.size(), characterId, equipType);
            return equips;
        } catch (Exception e) {
            log.error("Error retrieving equipments from Redis: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public int countEquip() {
        Set<String> keys = redisTemplate.keys("EQUIP_*");
        if (keys != null) {
            return keys.size();
        }
        return 0;
    }

}
