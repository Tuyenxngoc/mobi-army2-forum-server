package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.SpecialItem;

import java.util.Optional;

public interface SpecialItemRedisService {

    void addSpecialItem(SpecialItem specialItem);

    Optional<SpecialItem> getSpecialItem(Byte id);

    long countSpecialItems();

}
