package com.akshay.websockettask.security.strategy.username;

import com.akshay.websockettask.entity.AuthProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component()
public class GithubUsernameStrategy
        implements OAuthUsernameStrategy {

    @Override
    public String getUsername(OAuth2User user) {

        String login = user.getAttribute("login");

        if (login == null) {
            throw new RuntimeException("Login not found");
        }

        return login;
    }

    @Override
    public AuthProvider supports() {
        return AuthProvider.GITHUB;
    }
}
