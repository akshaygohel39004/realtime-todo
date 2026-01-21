package com.akshay.websockettask.service;

import com.akshay.websockettask.Exceptions.RefreshTokenException;
import com.akshay.websockettask.entity.RefreshToken;
import com.akshay.websockettask.entity.User;
import com.akshay.websockettask.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;


public interface RefreshTokenService {

   //token creation logic
    public RefreshToken create(User user, String token) ;
    //validating logic
    public RefreshToken validate(String token);
    //rotation logic
    public RefreshToken rotate(RefreshToken oldToken, String newTokenValue);
    public void revoke(RefreshToken token);

}
