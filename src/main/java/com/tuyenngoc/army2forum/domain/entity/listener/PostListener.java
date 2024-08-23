package com.tuyenngoc.army2forum.domain.entity.listener;

import com.tuyenngoc.army2forum.service.PostRedisService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class PostListener {

    private final PostRedisService postRedisService;

    @PrePersist
    public void prePersist(Object o) {

    }

    @PreUpdate
    public void preUpdate(Object o) {

    }

    @PreRemove
    public void preRemove(Object o) {

    }

    @PostLoad
    public void postLoad(Object o) {

    }

    @PostRemove
    public void postRemove(Object o) {
        postRedisService.clear();
    }

    @PostUpdate
    public void postUpdate(Object o) {
        postRedisService.clear();
    }

    @PostPersist
    public void postPersist(Object o) {
        postRedisService.clear();
    }

}
