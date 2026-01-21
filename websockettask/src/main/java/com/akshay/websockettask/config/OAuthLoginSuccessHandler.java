package com.akshay.websockettask.config;

import com.akshay.websockettask.DTO.authentication.jwtauth.LoginTokens;
import com.akshay.websockettask.DTO.authentication.oauth2.AuthContext;
import com.akshay.websockettask.Exceptions.OAuthAuthenticationException;
import com.akshay.websockettask.entity.AuthProvider;
import com.akshay.websockettask.entity.Role;
import com.akshay.websockettask.entity.RoleTypes;
import com.akshay.websockettask.entity.User;
import com.akshay.websockettask.repository.RoleRepository;
import com.akshay.websockettask.repository.UserRepository;
import com.akshay.websockettask.service.RefreshTokenService;
import com.akshay.websockettask.util.JWTUtil;
import com.akshay.websockettask.util.TokenCookieUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final TokenCookieUtil tokenCookieUtil;

    @Value("${auth.refresh.cookie.name}")
    private String REFRESH_TOKEN_NAME;

    @Value("${auth.access.cookie.name}")
    private String ACCESS_TOKEN_NAME;

    @Resource(name = "handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            processContinue(request, response, authentication);
        } catch (RuntimeException ex) {
            resolver.resolveException(request, response, null, ex);
        }
    }

    private void processContinue(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        OAuth2AuthenticationToken oauthToken = cast(authentication);
        AuthContext authContext = extractAuthContext(oauthToken);
        User user = findOrCreateUser(authContext);
        LoginTokens tokens = generateTokens(user);
        addCookies(response, tokens);
        redirect(response);
    }
    private OAuth2AuthenticationToken cast(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken token)) {
            throw new OAuthAuthenticationException("Invalid OAuth authentication token");
        }
        return token;
    }
    private AuthContext extractAuthContext(OAuth2AuthenticationToken token) {
        String provider = token.getAuthorizedClientRegistrationId();
        return switch (provider) {
            case "google" -> new AuthContext(
                    usernameFromGoogleAuth(token),
                    AuthProvider.GOOGLE
            );
            case "github" -> new AuthContext(
                    usernameFromGithubAuth(token),
                    AuthProvider.GITHUB
            );
            default -> throw new OAuthAuthenticationException("Unsupported OAuth provider");
        };
    }

    private User findOrCreateUser(AuthContext context) {
        if (context.username() == null) {
            throw new OAuthAuthenticationException("OAuth username is null");
        }

        return userRepository.findByUsername(context.username())
                .orElseGet(() -> createUser(context.username(), context.provider()));
    }

    private LoginTokens generateTokens(User user) {
        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        refreshTokenService.create(user, refreshToken);

        return new LoginTokens(accessToken, refreshToken);
    }

    private void addCookies(HttpServletResponse response, LoginTokens tokens) {
        tokenCookieUtil.add(response, tokens.refreshToken(), REFRESH_TOKEN_NAME);
        tokenCookieUtil.add(response, tokens.accessToken(), ACCESS_TOKEN_NAME);
    }

    private void redirect(HttpServletResponse response) {
        try {
            response.sendRedirect("/index.html");
        } catch (IOException ex) {
            throw new OAuthAuthenticationException("Redirect failed");
        }
    }

    private String usernameFromGoogleAuth(OAuth2AuthenticationToken token) {
        return token.getPrincipal().getAttribute("email");
    }

    private String usernameFromGithubAuth(OAuth2AuthenticationToken token) {
        return token.getPrincipal().getAttribute("login");
    }

    private User createUser(String username, AuthProvider authProvider) {
        Role role = roleRepository.findByType(RoleTypes.ROLE_USER)
                .orElseThrow(() -> new OAuthAuthenticationException("Default role not found"));

        User user = User.builder()
                .username(username)
                .password(null)
                .roles(Set.of(role))
                .authProvider(authProvider)
                .build();

        return userRepository.save(user);
    }

}
