package com.akshay.websockettask.DTO.authentication.jwtauth;

public record JWTResponse(String accessToken, String tokenType) {
    public JWTResponse(String accessToken) {
        this(accessToken, "Bearer");
    }
}
