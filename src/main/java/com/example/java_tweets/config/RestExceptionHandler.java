package com.example.java_tweets.config;

import com.example.java_tweets.config.exceptions.AuthException;
import com.example.java_tweets.config.exceptions.DataAccessException;
import com.example.java_tweets.config.exceptions.TweetNotFoundException;
import com.example.java_tweets.config.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<String> handleAuthException(AuthException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler(TweetNotFoundException.class)
    public ResponseEntity<String> handleDataAccessException(TweetNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
}
