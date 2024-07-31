package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.CategoryRequestDto;
import com.tuyenngoc.army2forum.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@Tag(name = "Category")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Get Category by Id for admin")
    @GetMapping(UrlConstant.Category.ADMIN_GET_BY_ID)
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        return VsResponseUtil.success(categoryService.getCategoryByIdForAdmin(id));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Get Categories for admin")
    @GetMapping(UrlConstant.Category.ADMIN_GET_ALL)
    public ResponseEntity<?> getCategoriesForAdmin(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(categoryService.getCategoriesForAdmin(requestDto));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Create Category")
    @PostMapping(UrlConstant.Category.CREATE)
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequestDto requestDto) {
        return VsResponseUtil.success(categoryService.createCategory(requestDto));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Update Category")
    @PutMapping(UrlConstant.Category.UPDATE)
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto requestDto
    ) {
        return VsResponseUtil.success(categoryService.updateCategory(id, requestDto));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Delete Category")
    @DeleteMapping(UrlConstant.Category.DELETE)
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        return VsResponseUtil.success(categoryService.deleteCategory(id));
    }

    @Operation(summary = "API Get Categories")
    @GetMapping(UrlConstant.Category.GET_ALL)
    public ResponseEntity<?> getCategories() {
        return VsResponseUtil.success(categoryService.getCategories());
    }

}
