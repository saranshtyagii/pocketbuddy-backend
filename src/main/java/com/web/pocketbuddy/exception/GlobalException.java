package com.web.pocketbuddy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public GlobalException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

}
