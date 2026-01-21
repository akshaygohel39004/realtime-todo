package com.akshay.websockettask.service;
import com.akshay.websockettask.Exceptions.RefreshTokenException;
import com.akshay.websockettask.entity.RefreshToken;
import com.akshay.websockettask.entity.User;
import com.akshay.websockettask.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PostgresRefreshTokenService implements  RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    //token creation logic
    @Override
    public RefreshToken create(User user, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    //validating logic
    @Override
    public RefreshToken validate(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token"));
        if (refreshToken.isRevoked()) {
            throw new RefreshTokenException("Refresh token revoked");
        }
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RefreshTokenException("Refresh token expired");
        }
        return refreshToken;
    }

    //rotation logic
    @Override
    public RefreshToken rotate(RefreshToken oldToken, String newTokenValue) {
        // revoke old token
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);
        // create new token
        return create(oldToken.getUser(), newTokenValue);
    }

    @Override
    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }
}
