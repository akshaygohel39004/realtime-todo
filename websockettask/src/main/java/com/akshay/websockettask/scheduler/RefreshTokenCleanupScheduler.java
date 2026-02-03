package com.akshay.websockettask.scheduler;

import com.akshay.websockettask.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    // Every day at 2 AM
    @Scheduled(cron = "${scheduler.refresh-token.cleanup}")
    public void cleanup() {
        int deleted = refreshTokenRepository.deleteByExpiryDateBeforeOrRevokedTrue(Instant.now());
        log.info("Deleted {} expired/revoked refresh tokens", deleted);
    }
}
