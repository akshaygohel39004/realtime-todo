package com.akshay.websockettask.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class OAuthAuthenticationException extends AuthenticationException {

    private final HttpStatus status;
    public OAuthAuthenticationException(String message) {
        super(message);
        this.status = HttpStatus.UNAUTHORIZED;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
