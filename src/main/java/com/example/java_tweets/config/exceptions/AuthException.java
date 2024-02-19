package com.example.java_tweets.config.exceptions;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;

    public AuthException(HttpStatus httpStatus, String message) {
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
