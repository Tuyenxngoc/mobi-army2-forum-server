package com.tuyenngoc.army2forum.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuyenngoc.army2forum.domain.entity.SpecialItem;
import com.tuyenngoc.army2forum.service.SpecialItemRedisService;
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
public class SpecialItemRedisServiceImpl implements SpecialItemRedisService {

    RedisTemplate<String, String> redisTemplate;

    ObjectMapper objectMapper;

    private String getKeyFrom(Byte id) {
        return String.format("SPECIAL_ITEM_%d", id).toUpperCase();
    }

    @Override
    public void addSpecialItem(SpecialItem specialItem) {
        try {
            String key = getKeyFrom(specialItem.getId());
            String json = objectMapper.writeValueAsString(specialItem);
            redisTemplate.opsForValue().set(key, json);
            log.info("Saved special item with name: {} to Redis", specialItem.getName());
        } catch (Exception e) {
            log.error("Error saving special item to Redis: {}", e.getMessage(), e);
        }
    }

    @Override
    public Optional<SpecialItem> getSpecialItem(Byte id) {
        try {
            String key = getKeyFrom(id);
            String json = redisTemplate.opsForValue().get(key);
            if (StringUtils.hasText(json)) {
                SpecialItem specialItem = objectMapper.readValue(json, SpecialItem.class);
                return Optional.of(specialItem);
            } else {
                log.warn("No special item found in Redis for key: {}", key);
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Error retrieving special item from Redis: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<SpecialItem> getSpecialItems() {
        try {
            String keyPattern = "SPECIAL_ITEM_*";
            Set<String> keys = redisTemplate.keys(keyPattern);
            if (keys == null || keys.isEmpty()) {
                log.warn("No special items found with key pattern: {}", keyPattern);
                return List.of();
            }

            List<SpecialItem> specialItems = new ArrayList<>();
            for (String key : keys) {
                String json = redisTemplate.opsForValue().get(key);
                if (StringUtils.hasText(json)) {
                    SpecialItem specialItem = objectMapper.readValue(json, SpecialItem.class);
                    specialItems.add(specialItem);
                }
            }

            return specialItems;
        } catch (Exception e) {
            log.error("Error retrieving special items from Redis: {}", e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public long countSpecialItems() {
        Set<String> keys = redisTemplate.keys("SPECIAL_ITEM_*");
        if (keys != null) {
            return keys.size();
        }
        return 0;
    }

}
