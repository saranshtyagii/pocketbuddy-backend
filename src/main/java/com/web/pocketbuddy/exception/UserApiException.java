package com.web.pocketbuddy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserApiException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public UserApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
