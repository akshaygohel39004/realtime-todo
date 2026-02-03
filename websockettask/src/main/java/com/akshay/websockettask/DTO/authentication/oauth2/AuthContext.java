package com.akshay.websockettask.dto.authentication.oauth2;

import com.akshay.websockettask.entity.type.AuthProvider;

public record AuthContext(String username, AuthProvider provider) {}