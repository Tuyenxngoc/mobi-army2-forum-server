package com.tuyenngoc.army2forum.domain.dto.request.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequestDto {

    private String title;

    private String content;

    private Long categoryId;

    private Integer priority;

}
