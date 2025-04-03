package com.web.pocketbuddy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotificationException extends RuntimeException {

    private String message;
    private HttpStatus status;

    public NotificationException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.status = httpStatus;
    }
}
