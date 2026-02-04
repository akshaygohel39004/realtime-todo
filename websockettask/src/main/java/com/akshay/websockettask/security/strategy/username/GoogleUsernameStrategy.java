package com.akshay.websockettask.security.strategy.username;

import com.akshay.websockettask.entity.type.AuthProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GoogleUsernameStrategy
        implements OAuthUsernameStrategy {

    @Override
    public String getUsername(OAuth2User user) {

        String email = user.getAttribute("email");

        if (email == null) {
            throw new RuntimeException("Email not found");
        }

        return email;
    }

    @Override
    public AuthProvider supports() {
        return AuthProvider.GOOGLE;
    }
}
