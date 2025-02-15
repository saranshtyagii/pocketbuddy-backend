package com.web.pocketbuddy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GroupApiExceptions extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;

    public GroupApiExceptions(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
