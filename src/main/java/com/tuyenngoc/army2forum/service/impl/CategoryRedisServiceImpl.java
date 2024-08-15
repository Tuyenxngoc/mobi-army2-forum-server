package com.tuyenngoc.army2forum.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuyenngoc.army2forum.domain.dto.CategoryDto;
import com.tuyenngoc.army2forum.service.CategoryRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryRedisServiceImpl implements CategoryRedisService {

    static String CATEGORIES_KEY = "CATEGORIES";

    static final TypeReference<List<CategoryDto>> CATEGORY_LIST_TYPE = new TypeReference<>() {
    };

    RedisTemplate<String, String> redisTemplate;

    ObjectMapper objectMapper;

    private String getKey() {
        return CATEGORIES_KEY;
    }

    @Override
    public void clear() {
        redisTemplate.delete(getKey());
        log.info("Cleared categories from Redis.");
    }

    @Override
    public List<CategoryDto> getCategories() {
        try {
            String json = redisTemplate.opsForValue().get(getKey());
            if (StringUtils.hasText(json)) {
                return objectMapper.readValue(json, CATEGORY_LIST_TYPE);
            }
            return null;
        } catch (Exception e) {
            log.error("Error retrieving categories from Redis: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void addCategories(List<CategoryDto> categoryDtos) {
        try {
            redisTemplate.opsForValue().set(getKey(), objectMapper.writeValueAsString(categoryDtos));
            log.info("Categories added to Redis.");
        } catch (Exception e) {
            log.error("Error serializing and adding categories to Redis: {}", e.getMessage(), e);
        }
    }

}