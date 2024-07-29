package com.tuyenngoc.army2forum.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class IntArrayJsonConverter extends JsonAttributeConverter<int[]> {

    public IntArrayJsonConverter() {
        super(new TypeReference<>() {
        });
    }

}
