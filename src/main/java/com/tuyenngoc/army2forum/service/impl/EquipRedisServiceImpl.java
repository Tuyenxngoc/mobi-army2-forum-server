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
    public int countEquip() {
        Set<String> keys = redisTemplate.keys("EQUIP_*");
        if (keys != null) {
            return keys.size();
        }
        return 0;
    }

}
