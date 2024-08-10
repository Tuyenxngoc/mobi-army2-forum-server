package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.CategoryDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.CategoryRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.category.GetCategoryResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Category;
import com.tuyenngoc.army2forum.domain.mapper.CategoryMapper;
import com.tuyenngoc.army2forum.domain.specification.CategorySpecification;
import com.tuyenngoc.army2forum.exception.BadRequestException;
import com.tuyenngoc.army2forum.exception.ConflictException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.CategoryRepository;
import com.tuyenngoc.army2forum.service.CategoryService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    MessageSource messageSource;

    CategoryMapper categoryMapper;

    CategoryRepository categoryRepository;

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

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, categoryId));
    }

    @Override
    public List<CategoryDto> getCategories() {
        return categoryRepository.getAllCategories();
    }

    @Override
    public CategoryDto getCategoryByIdForAdmin(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Category.ERR_NOT_FOUND_ID, categoryId));

        return new CategoryDto(category);
    }

    @Override
    public PaginationResponseDto<GetCategoryResponseDto> getCategoriesForAdmin(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.CATEGORY);

        Page<Category> page = categoryRepository.findAll(
                CategorySpecification.filterCategories(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable
        );

        List<GetCategoryResponseDto> items = page.getContent().stream()
                .map(GetCategoryResponseDto::new)
                .collect(Collectors.toList());

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.CATEGORY, page);

        PaginationResponseDto<GetCategoryResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public CommonResponseDto createCategory(CategoryRequestDto requestDto) {
        boolean existsByName = categoryRepository.existsByName(requestDto.getName());
        if (existsByName) {
            throw new ConflictException(ErrorMessage.Category.ERR_DUPLICATE_NAME, requestDto.getName());
        }

        Category category = categoryMapper.toCategory(requestDto);

        categoryRepository.save(category);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto updateCategory(Long categoryId, CategoryRequestDto requestDto) {
        boolean existsByName = categoryRepository.existsByName(requestDto.getName());
        if (existsByName) {
            throw new ConflictException(ErrorMessage.Category.ERR_DUPLICATE_NAME, requestDto.getName());
        }

        Category category = categoryMapper.toCategory(requestDto);
        category.setId(categoryId);

        categoryRepository.save(category);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto deleteCategory(Long categoryId) {
        Category category = getCategoryById(categoryId);

        if (!category.getPosts().isEmpty()) {
            throw new BadRequestException(ErrorMessage.Category.ERR_NOT_EMPTY);
        }

        categoryRepository.delete(category);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }
}
