package com.tuyenngoc.army2forum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BadGatewayException extends RuntimeException {

    private Object[] params;

    public BadGatewayException(String message) {
        super(message);
    }

    public BadGatewayException(String message, Object... params) {
        super(message);
        this.params = params;
    }
}
