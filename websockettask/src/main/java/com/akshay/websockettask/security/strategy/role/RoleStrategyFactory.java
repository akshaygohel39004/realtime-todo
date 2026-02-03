package com.akshay.websockettask.security.strategy.role;

import com.akshay.websockettask.Exceptions.OAuthAuthenticationException;
import com.akshay.websockettask.entity.type.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleStrategyFactory {

    private final ApplicationContext context;

    public RoleStrategy getStrategy(AuthProvider provider) {

        try {
            return context.getBean("ROLE_"+provider.name(), RoleStrategy.class);
        }
        catch (Exception e) {

            throw new OAuthAuthenticationException(
                    "Unsupported OAuth provider"
            );
        }
    }
}
