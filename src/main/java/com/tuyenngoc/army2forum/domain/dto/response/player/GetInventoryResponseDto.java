package com.tuyenngoc.army2forum.domain.dto.response.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetInventoryResponseDto {

    private List<GetEquipmentResponseDto> equipments = new ArrayList<>();

    private List<GetSpecialItemResponseDto> items = new ArrayList<>();

}
