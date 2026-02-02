package com.akshay.websockettask.dto.authentication.oauth2;

import com.akshay.websockettask.entity.AuthProvider;

public record AuthContext(String username, AuthProvider provider) {}