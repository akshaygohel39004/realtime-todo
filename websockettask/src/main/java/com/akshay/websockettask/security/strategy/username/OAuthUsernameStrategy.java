package com.akshay.websockettask.security.strategy.username;

import com.akshay.websockettask.entity.type.AuthProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuthUsernameStrategy {

    String getUsername(OAuth2User user);

    AuthProvider supports();
}
