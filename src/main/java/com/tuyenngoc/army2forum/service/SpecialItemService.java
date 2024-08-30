package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.SpecialItem;

import java.util.List;

public interface SpecialItemService {

    void initCacheSpecialItems();

    List<SpecialItem> getSpecialItem();

}
