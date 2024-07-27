package com.tuyenngoc.army2forum.domain.mapper;

import com.tuyenngoc.army2forum.domain.dto.request.CategoryRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(CategoryRequestDto requestDto);

}
