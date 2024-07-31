package com.tuyenngoc.army2forum.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tuyenngoc.army2forum.domain.json.ClanItem;
import jakarta.persistence.Converter;

import java.util.List;

@Converter(autoApply = true)
public class ClanItemConverter extends JsonAttributeConverter<List<ClanItem>> {

    public ClanItemConverter() {
        super(new TypeReference<>() {
        });
    }
}
