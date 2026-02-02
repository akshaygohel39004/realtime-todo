package com.akshay.websockettask.dto.authentication.signup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(


        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 20, message = "Username must be 4–20 characters")
        String userName,

        @NotBlank(message = "Password is required")
        @Size(min = 4, message = "Password must be at least 4 characters")
        String password

) {}