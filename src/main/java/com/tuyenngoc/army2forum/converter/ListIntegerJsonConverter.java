package com.tuyenngoc.army2forum.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;

import java.util.List;

@Converter(autoApply = true)
public class ListIntegerJsonConverter extends JsonAttributeConverter<List<Integer>> {

    public ListIntegerJsonConverter() {
        super(new TypeReference<>() {
        });
    }

}
