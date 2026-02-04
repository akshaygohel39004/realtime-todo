package com.akshay.websockettask.dto.authentication.jwtauth;

public record JWTResponse(String accessToken, String tokenType) {
    public JWTResponse(String accessToken) {
        this(accessToken, "Bearer");
    }
}
