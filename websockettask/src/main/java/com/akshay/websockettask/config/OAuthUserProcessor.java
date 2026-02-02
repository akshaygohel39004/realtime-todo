package com.akshay.websockettask.config;

import com.akshay.websockettask.entity.*;
import com.akshay.websockettask.entity.type.AuthProvider;
import com.akshay.websockettask.repository.UserRepository;
import com.akshay.websockettask.security.strategy.role.RoleStrategyFactory;
import com.akshay.websockettask.security.strategy.username.UsernameStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OAuthUserProcessor {

    private final UserRepository userRepository;
    private final RoleStrategyFactory roleStrategyFactory;
    private final UsernameStrategyFactory usernameStrategyFactory;

    public User processUser(
            String registrationId,
            OAuth2User oAuth2User
    ) {

        // Provider
        AuthProvider provider =
                AuthProvider.valueOf(registrationId.toUpperCase());

        // Username
        String username =
                usernameStrategyFactory
                        .getStrategy(provider)
                        .getUsername(oAuth2User);

        // Find/Create
        return userRepository
                .findByUsername(username)
                .orElseGet(() -> createUser(username, provider));
    }

    private User createUser(String username, AuthProvider provider) {

        Set<Role> roles =
                roleStrategyFactory
                        .getStrategy(provider)
                        .getRoles();

        User user = User.builder()
                .username(username)
                .authProvider(provider)
                .roles(roles)
                .build();

        return userRepository.save(user);
    }
}
