package com.tuyenngoc.army2forum.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tuyenngoc.army2forum.domain.json.EquipChest;
import jakarta.persistence.Converter;

import java.util.List;

@Converter(autoApply = true)
public class EquipmentChestConverter extends JsonAttributeConverter<List<EquipChest>> {

    public EquipmentChestConverter() {
        super(new TypeReference<>() {
        });
    }

}
