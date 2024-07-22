package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.entity.Category;
import com.tuyenngoc.army2forum.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@Tag(name = "Category")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Get Category by Id")
    @GetMapping(UrlConstant.Category.GET_BY_ID)
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return VsResponseUtil.success(categoryService.getCategoryById(id));
    }

    @Operation(summary = "API Get All Categories")
    @GetMapping(UrlConstant.Category.GET_ALL)
    public ResponseEntity<?> getAllCategories() {
        return VsResponseUtil.success(categoryService.getAllCategories());
    }

}
