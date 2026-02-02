package com.akshay.websockettask.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService
        extends OidcUserService {

    private final OAuthUserProcessor processor;

    @Override
    public OidcUser loadUser(OidcUserRequest request)
            throws OAuth2AuthenticationException {

        OidcUser oidcUser = super.loadUser(request);
        processor.processUser(
                request.getClientRegistration()
                        .getRegistrationId(),
                oidcUser
        );

        return oidcUser;
    }
}
