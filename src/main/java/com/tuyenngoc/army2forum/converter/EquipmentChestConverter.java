package com.tuyenngoc.army2forum.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tuyenngoc.army2forum.domain.entity.Equip;
import jakarta.persistence.Converter;

import java.util.List;

@Converter(autoApply = true)
public class EquipmentChestConverter extends JsonAttributeConverter<List<Equip>> {

    public EquipmentChestConverter() {
        super(new TypeReference<>() {
        });
    }

}
