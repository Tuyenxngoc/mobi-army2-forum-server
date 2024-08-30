package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.entity.SpecialItem;
import com.tuyenngoc.army2forum.repository.SpecialItemRepository;
import com.tuyenngoc.army2forum.service.SpecialItemRedisService;
import com.tuyenngoc.army2forum.service.SpecialItemService;
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
public class SpecialItemServiceImpl implements SpecialItemService {

    SpecialItemRepository specialItemRepository;

    SpecialItemRedisService specialItemRedisService;

    @Override
    public void initCacheSpecialItems() {
        if (specialItemRedisService.countSpecialItems() != specialItemRepository.count()) {
            List<SpecialItem> specialItems = specialItemRepository.findAll();

            for (SpecialItem specialItem : specialItems) {
                specialItemRedisService.addSpecialItem(specialItem);
            }
            log.info("Initialized cache for {} special items.", specialItems.size());
        } else {
            log.info("Special item cache is already up to date.");
        }
    }

    @Override
    public List<SpecialItem> getSpecialItem() {
        return specialItemRedisService.getSpecialItems();
    }

}
