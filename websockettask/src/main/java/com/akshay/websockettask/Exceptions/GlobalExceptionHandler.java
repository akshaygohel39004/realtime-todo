package com.akshay.websockettask.Exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(
            ApiException ex,
            HttpServletRequest request) {

        return error(
                ex.getMessage(),
                ex.getStatus(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String,Object>> handleBadCredentials(BadCredentialsException badCredentialsException, HttpServletRequest request){
        return error(
                badCredentialsException.getMessage(),
                HttpStatus.UNAUTHORIZED,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(OAuthAuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleOAuthAuthenticationException(OAuthAuthenticationException ex, HttpServletRequest request) {
        return error(
                ex.getMessage(),
                ex.getStatus(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex, HttpServletRequest request) {
        return error(
                "Something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(err ->
                        fieldErrors.put(err.getField(), err.getDefaultMessage())
                );

        return error(
                fieldErrors.toString(),
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
    }


    private ResponseEntity<Map<String, Object>> error(String message, HttpStatus status, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("isSuccess", false);                   //always be false
        body.put("timestamp", Instant.now());           // ISO-8601 universal time
        body.put("status", status.value());             // eg. 404
        body.put("error", status.getReasonPhrase());    // eg. Not Found
        body.put("message", message);                   // Custom msg
        body.put("path", path);                         // /api/todos/{id}
        return ResponseEntity.status(status).body(body);
    }
}
