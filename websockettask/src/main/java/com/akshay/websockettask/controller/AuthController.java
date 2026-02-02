package com.akshay.websockettask.controller;

import com.akshay.websockettask.dto.authentication.jwtauth.JWTRequest;
import com.akshay.websockettask.dto.authentication.jwtauth.JWTResponse;
import com.akshay.websockettask.dto.authentication.jwtauth.LoginTokens;
import com.akshay.websockettask.dto.authentication.signup.SignupRequest;
import com.akshay.websockettask.dto.authentication.signup.SignupResponse;
import com.akshay.websockettask.service.AuthService;
import com.akshay.websockettask.util.TokenCookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenCookieUtil tokenCookieUtil;

    @Value("${auth.refresh.cookie.name}")
    private String REFRESH_TOKEN_NAME;


    // user signup
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    // login and issue tokens
    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request, HttpServletResponse response) {
        LoginTokens tokens = authService.login(request);
        tokenCookieUtil.add(response, tokens.refreshToken(),REFRESH_TOKEN_NAME);
        return ResponseEntity.ok(new JWTResponse(tokens.accessToken()));
    }

    // generate new access token using refresh token
    @PostMapping("/refresh")
    public ResponseEntity<JWTResponse> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = tokenCookieUtil.extract(request,REFRESH_TOKEN_NAME);
        LoginTokens tokens = authService.refresh(refreshToken);
        tokenCookieUtil.add(response, tokens.refreshToken(),REFRESH_TOKEN_NAME);
        return ResponseEntity.ok(new JWTResponse(tokens.accessToken()));
    }

    // logout current session
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = tokenCookieUtil.extract(request,REFRESH_TOKEN_NAME);
        authService.logout(refreshToken);
        tokenCookieUtil.clear(response,REFRESH_TOKEN_NAME);
        return ResponseEntity.ok("Logged out successfully");
    }
}
