package com.akshay.websockettask.Exceptions;

import org.springframework.http.HttpStatus;

public class RefreshTokenException extends ApiException {
    public RefreshTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}