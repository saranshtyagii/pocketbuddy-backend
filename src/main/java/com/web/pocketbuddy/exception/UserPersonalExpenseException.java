package com.web.pocketbuddy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserPersonalExpenseException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

    public UserPersonalExpenseException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

}
