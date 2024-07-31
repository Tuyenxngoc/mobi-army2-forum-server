package com.tuyenngoc.army2forum.domain.dto.response;

import com.tuyenngoc.army2forum.domain.entity.Clan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetClanResponseDto {

    private String name;

    public GetClanResponseDto(Clan clan) {
        this.name = clan.getName();
    }

}
