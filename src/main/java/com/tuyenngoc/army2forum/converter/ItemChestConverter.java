package com.tuyenngoc.army2forum.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tuyenngoc.army2forum.domain.entity.SpecialItem;
import jakarta.persistence.Converter;

import java.util.List;

@Converter(autoApply = true)
public class ItemChestConverter extends JsonAttributeConverter<List<SpecialItem>> {

    protected ItemChestConverter() {
        super(new TypeReference<>() {
        });
    }

}
