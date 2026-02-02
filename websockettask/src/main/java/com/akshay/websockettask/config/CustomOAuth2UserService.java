package com.akshay.websockettask.config;

import com.akshay.websockettask.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService
        extends DefaultOAuth2UserService {

    private final OAuthUserProcessor processor;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);

        User user = processor.processUser(
                request.getClientRegistration()
                        .getRegistrationId(),
                oAuth2User
        );

        List<SimpleGrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(
                                r.getType().name()))
                        .toList();

        String nameAttr =
                request.getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();

        return new DefaultOAuth2User(
                authorities,
                oAuth2User.getAttributes(),
                nameAttr
        );
    }
}
