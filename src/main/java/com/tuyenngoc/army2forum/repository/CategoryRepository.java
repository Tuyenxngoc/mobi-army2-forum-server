package com.tuyenngoc.army2forum.repository;

import com.tuyenngoc.army2forum.domain.dto.CategoryDto;
import com.tuyenngoc.army2forum.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT new com.tuyenngoc.army2forum.domain.dto.CategoryDto(c) FROM " +
            "Category c ")
    List<CategoryDto> getAllCategories();

}
