package com.tuyenngoc.army2forum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotFoundException extends RuntimeException {

    private Object[] params;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Object... params) {
        super(message);
        this.params = params;
    }

}
