package com.web.pocketbuddy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthenticationException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public AuthenticationException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

}
