package com.akshay.websockettask.security.strategy.username;

import com.akshay.websockettask.Exceptions.OAuthAuthenticationException;
import com.akshay.websockettask.entity.type.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UsernameStrategyFactory {

    private final List<OAuthUsernameStrategy> strategies;

    public OAuthUsernameStrategy getStrategy(AuthProvider provider) {

        return strategies.stream()
                .filter(s -> s.supports() == provider)
                .findFirst()
                .orElseThrow(() ->
                        new OAuthAuthenticationException("Unsupported provider")
                );
    }
}
