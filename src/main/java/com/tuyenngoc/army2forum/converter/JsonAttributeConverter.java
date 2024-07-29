package com.tuyenngoc.army2forum.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

public abstract class JsonAttributeConverter<T> implements AttributeConverter<T, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final TypeReference<T> typeReference;

    protected JsonAttributeConverter(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public String convertToDatabaseColumn(T attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting to JSON", e);
        }
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return objectMapper.readValue(dbData, typeReference);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting from JSON", e);
        }
    }
}
