package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.service.EquipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Equipment")
public class EquipController {

    EquipService equipService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "API Get Equips By Character Id and Equip Type")
    @GetMapping(UrlConstant.Equip.GET_BY_CHARACTER_ID_AND_TYPE)
    public ResponseEntity<?> getEquipsByCharacterIdAndType(@PathVariable Byte characterId, @PathVariable Byte type) {
        return VsResponseUtil.success(equipService.getEquipsByCharacterIdAndType(characterId, type));
    }

}
