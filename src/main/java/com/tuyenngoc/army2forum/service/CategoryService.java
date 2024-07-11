package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.domain.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);

    Category updateCategory(Long id, Category category);

    void deleteCategory(Long id);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();

    void initCategories(AdminInfo adminInfo);
}
