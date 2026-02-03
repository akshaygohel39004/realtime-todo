package com.akshay.websockettask.repository;

import com.akshay.websockettask.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);


    @Transactional
    int deleteByExpiryDateBeforeOrRevokedTrue(Instant now);
}
