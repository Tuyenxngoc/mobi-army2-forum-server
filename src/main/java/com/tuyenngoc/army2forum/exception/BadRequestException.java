package com.tuyenngoc.army2forum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BadRequestException extends RuntimeException {

    private Object[] params;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Object... args) {
        super(message);
        this.params = args;
    }

}