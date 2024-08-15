package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.CategoryDto;

import java.util.List;

public interface CategoryRedisService {

    void clear();

    List<CategoryDto> getCategories();

    void addCategories(List<CategoryDto> categoryDtos);

}
