package com.akshay.websockettask.config;

import com.akshay.websockettask.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService
        extends OidcUserService {

    private final OAuthUserProcessor processor;

    @Override
    public OidcUser loadUser(OidcUserRequest request)
            throws OAuth2AuthenticationException {

        OidcUser oidcUser = super.loadUser(request);
        User user=processor.processUser(
                request.getClientRegistration()
                        .getRegistrationId(),
                oidcUser
        );

        // Map roles
        Set<GrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(
                                r.getType().name()))
                        .collect(Collectors.toSet());

        // Keep default authorities (SCOPE_*)
        authorities.addAll(oidcUser.getAuthorities());

        return new DefaultOidcUser(
                authorities,
                oidcUser.getIdToken(),
                oidcUser.getUserInfo()
        );
    }
}
