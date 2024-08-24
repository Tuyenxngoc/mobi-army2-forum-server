package com.tuyenngoc.army2forum.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto;
import com.tuyenngoc.army2forum.service.PostRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostRedisServiceImpl implements PostRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    private static final TypeReference<PaginationResponseDto<GetPostResponseDto>> POSTS_LIST_RESPONSE_TYPE =
            new TypeReference<>() {
            };

    private String getKeyFrom(Long postId) {
        return String.format("POST_DETAIL:%d", postId).toUpperCase();
    }

    private String getKeyFrom(Long categoryId, Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        return String.format("ALL_POSTS:%d:%d:%d", categoryId, pageNumber, pageSize).toUpperCase();
    }

    @Override
    public void clear() {
        Set<String> keys = redisTemplate.keys("ALL_POSTS:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Cleared all keys starting with 'ALL_POSTS'");
        } else {
            log.info("No keys found starting with 'ALL_POSTS'");
        }
    }

    @Override
    public void savePost(Long categoryId, Pageable pageable, PaginationResponseDto<GetPostResponseDto> responseDto) {
        try {
            String key = getKeyFrom(categoryId, pageable);
            String json = objectMapper.writeValueAsString(responseDto);
            redisTemplate.opsForValue().set(key, json);
        } catch (Exception e) {
            log.error("Error saving post details to Redis: {}", e.getMessage(), e);
        }
    }

    @Override
    public PaginationResponseDto<GetPostResponseDto> getPostsByCategoryId(Long categoryId, Pageable pageable) {
        try {
            String key = getKeyFrom(categoryId, pageable);
            String json = redisTemplate.opsForValue().get(key);

            if (StringUtils.hasText(json)) {
                return objectMapper.readValue(json, POSTS_LIST_RESPONSE_TYPE);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching posts from Redis: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void deletePost(Long postId) {
        try {
            String key = getKeyFrom(postId);
            Boolean deleted = redisTemplate.delete(key);
            if (Boolean.TRUE.equals(deleted)) {
                log.info("Deleted post with ID: {}", postId);
            } else {
                log.info("No post found with ID: {}", postId);
            }
        } catch (Exception e) {
            log.error("Error deleting post from Redis: {}", e.getMessage(), e);
        }
    }

    @Override
    public void savePost(GetPostDetailResponseDto post) {
        try {
            Long postId = post.getId();
            String key = getKeyFrom(postId);
            String json = objectMapper.writeValueAsString(post);
            redisTemplate.opsForValue().set(key, json);
            log.info("Saved post with ID: {} to Redis", postId);
        } catch (Exception e) {
            log.error("Error saving post details to Redis: {}", e.getMessage(), e);
        }
    }

    @Override
    public GetPostDetailResponseDto getPostById(Long postId) {
        try {
            String key = getKeyFrom(postId);
            String json = redisTemplate.opsForValue().get(key);

            if (StringUtils.hasText(json)) {
                return objectMapper.readValue(json, GetPostDetailResponseDto.class);
            } else {
                log.info("No post found with ID: {}", postId);
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching post from Redis: {}", e.getMessage(), e);
            return null;
        }
    }

}
