package com.akshay.websockettask.bootstrap;

import com.akshay.websockettask.entity.type.AuthProvider;
import com.akshay.websockettask.repository.RoleRepository;
import com.akshay.websockettask.repository.UserRepository;
import com.akshay.websockettask.entity.Role;
import com.akshay.websockettask.entity.type.RoleTypes;
import com.akshay.websockettask.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${app.admin.username}")
    private String adminUsername;
    @Value("${app.admin.password}")
    private String adminPassword;
    @Value("${app.user.username}")
    private String userUsername;
    @Value("${app.user.password}")
    private String userPassword;
    @Override
    public void run(String... args) {

        Role userRole = getOrCreateRole(RoleTypes.ROLE_USER);
        Role adminRole = getOrCreateRole(RoleTypes.ROLE_ADMIN);

        //initial two users one is for simple User role and another one is for Admin role
        createUserIfNotExists(userUsername, userPassword, userRole);
        createUserIfNotExists(adminUsername, adminPassword, adminRole);
    }

    private Role getOrCreateRole(RoleTypes roleType) {
        return roleRepository.findByType(roleType)
                .orElseGet(() ->
                        roleRepository.save(new Role(null, roleType))
                );
    }

    private void createUserIfNotExists(String username, String rawPassword, Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            return;
        }
        User user = User
                .builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .roles(Set.of(role))
                .authProvider(AuthProvider.FORM_LOGIN)
                .build();

        userRepository.save(user);
    }
}
