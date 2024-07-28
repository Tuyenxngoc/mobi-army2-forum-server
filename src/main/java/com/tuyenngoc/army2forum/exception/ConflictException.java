package com.tuyenngoc.army2forum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConflictException extends RuntimeException {

    private Object[] params;

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Object... params) {
        super(message);
        this.params = params;
    }
}