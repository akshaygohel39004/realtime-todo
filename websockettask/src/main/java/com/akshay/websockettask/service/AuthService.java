package com.akshay.websockettask.service;

import com.akshay.websockettask.DTO.authentication.jwtauth.JWTRequest;
import com.akshay.websockettask.DTO.authentication.jwtauth.LoginTokens;
import com.akshay.websockettask.DTO.authentication.signup.SignupRequest;
import com.akshay.websockettask.DTO.authentication.signup.SignupResponse;

public interface AuthService {

    public SignupResponse signup(SignupRequest request) ;

    public LoginTokens login(JWTRequest request) ;

    public LoginTokens refresh(String refreshTokenFromCookie) ;

    public void logout(String refreshTokenFromCookie) ;

}
