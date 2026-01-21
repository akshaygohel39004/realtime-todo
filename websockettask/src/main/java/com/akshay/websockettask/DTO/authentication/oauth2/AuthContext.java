package com.akshay.websockettask.DTO.authentication.oauth2;

import com.akshay.websockettask.entity.AuthProvider;

public record AuthContext(String username, AuthProvider provider) {}