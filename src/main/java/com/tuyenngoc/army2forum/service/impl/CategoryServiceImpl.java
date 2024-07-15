package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.domain.dto.CategoryDto;
import com.tuyenngoc.army2forum.domain.entity.Category;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.CategoryRepository;
import com.tuyenngoc.army2forum.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category updatedCategory = existingCategory.get();
            updatedCategory.setName(category.getName());
            updatedCategory.setDescription(category.getDescription());
            return categoryRepository.save(updatedCategory);
        }
        return null;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    @Override
    public void initCategories(AdminInfo adminInfo) {
        if (categoryRepository.count() == 0) {
            String[] titles = new String[]{"Báo lỗi", "Tố cáo", "Góp ý"};
            for (String title : titles) {
                Category category = new Category(title);
                category.setCreatedBy(adminInfo.getUsername());
                category.setLastModifiedBy(adminInfo.getUsername());
                categoryRepository.save(category);
            }

            log.info("Added category " + categoryRepository.count() + " categories");
        }
    }
}
