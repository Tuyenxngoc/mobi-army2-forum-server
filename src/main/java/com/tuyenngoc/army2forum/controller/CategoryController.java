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

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Create Category")
    @PostMapping(UrlConstant.Category.CREATE)
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        return VsResponseUtil.success(categoryService.createCategory(category));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Update Category")
    @PutMapping(UrlConstant.Category.UPDATE)
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return VsResponseUtil.success(categoryService.updateCategory(id, category));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "API Delete Category")
    @DeleteMapping(UrlConstant.Category.DELETE)
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return VsResponseUtil.success(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
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
