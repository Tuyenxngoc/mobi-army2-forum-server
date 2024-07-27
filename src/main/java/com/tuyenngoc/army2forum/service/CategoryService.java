package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.domain.dto.CategoryDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.CategoryRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.category.GetCategoryResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Category;

import java.util.List;

public interface CategoryService {

    void initCategories(AdminInfo adminInfo);

    Category getCategoryById(Long categoryId);

    List<CategoryDto> getCategories();

    CategoryDto getCategoryByIdForAdmin(Long categoryId);

    PaginationResponseDto<GetCategoryResponseDto> getCategoriesForAdmin(PaginationFullRequestDto requestDto);

    CommonResponseDto createCategory(CategoryRequestDto requestDto);

    CommonResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto);

    CommonResponseDto deleteCategory(Long categoryId);

}
