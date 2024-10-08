package com.tuyenngoc.army2forum.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tuyenngoc.army2forum.domain.json.SpecialItemChest;
import jakarta.persistence.Converter;

import java.util.List;

@Converter(autoApply = true)
public class ItemChestConverter extends JsonAttributeConverter<List<SpecialItemChest>> {

    public ItemChestConverter() {
        super(new TypeReference<>() {
        });
    }

}
