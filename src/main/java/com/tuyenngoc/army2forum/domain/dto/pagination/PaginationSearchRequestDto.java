package com.tuyenngoc.army2forum.domain.dto.pagination;

import com.tuyenngoc.army2forum.constant.CommonConstant;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationSearchRequestDto extends PaginationRequestDto {

    @Parameter(description = "Keyword to search")
    private String keyword = CommonConstant.EMPTY_STRING;

    public String getKeyword() {
        return keyword.trim();
    }

}
