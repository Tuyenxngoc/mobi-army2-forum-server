package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.domain.dto.CategoryDto;
import com.tuyenngoc.army2forum.domain.entity.Category;

import java.util.List;

public interface CategoryService {

    Category getCategoryById(Long id);

    List<CategoryDto> getAllCategories();

    void initCategories(AdminInfo adminInfo);
}
