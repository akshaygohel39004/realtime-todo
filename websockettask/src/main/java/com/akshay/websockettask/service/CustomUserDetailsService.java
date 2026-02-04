package com.akshay.websockettask.service;

import com.akshay.websockettask.entity.type.AuthProvider;
import com.akshay.websockettask.entity.User;
import com.akshay.websockettask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user= userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //if we implement password update mechanism so we need add condition password should null here for through exception
        if (user.getAuthProvider() != AuthProvider.FORM_LOGIN) {
            throw new BadCredentialsException(
                    "This account uses social login. please sign in with oauth2."
            );
        }
        return user;
    }

}
