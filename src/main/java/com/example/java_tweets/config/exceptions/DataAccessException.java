package com.example.java_tweets.config.exceptions;

import org.springframework.http.HttpStatus;

public class DataAccessException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public DataAccessException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
