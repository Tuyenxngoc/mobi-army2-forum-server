package com.tuyenngoc.army2forum.domain.dto.response.category;

import com.tuyenngoc.army2forum.domain.dto.common.UserDateAuditingDto;
import com.tuyenngoc.army2forum.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryResponseDto extends UserDateAuditingDto {

    private long id;

    private String name;

    private int postCount;

    public GetCategoryResponseDto(Category category) {
        this.setCreatedDate(category.getCreatedDate());
        this.setLastModifiedDate(category.getLastModifiedDate());
        this.setCreatedBy(category.getCreatedBy());
        this.setLastModifiedBy(category.getLastModifiedBy());

        this.id = category.getId();
        this.name = category.getName();
        this.postCount = category.getPosts().size();
    }
}
