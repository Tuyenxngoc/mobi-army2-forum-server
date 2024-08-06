package com.tuyenngoc.army2forum.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClanMemberDto {

    private int rights;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClanDto clan;

}
