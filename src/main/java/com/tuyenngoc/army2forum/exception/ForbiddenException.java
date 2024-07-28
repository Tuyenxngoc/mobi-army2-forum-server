package com.tuyenngoc.army2forum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ResponseStatus(HttpStatus.UNAUTHORIZED)

public class ForbiddenException extends RuntimeException {

    private Object[] params;

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Object... params) {
        super(message);
        this.params = params;
    }

}