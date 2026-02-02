package com.akshay.websockettask.security.strategy.role;

import com.akshay.websockettask.entity.Role;
import com.akshay.websockettask.entity.type.RoleTypes;
import com.akshay.websockettask.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("ROLE_FORM_LOGIN")
@RequiredArgsConstructor
public class FormLoginRoleStrategy implements RoleStrategy {

    private final RoleRepository roleRepository;

    @Override
    public Set<Role> getRoles() {

        Role userRole = roleRepository
                .findByType(RoleTypes.ROLE_USER)
                .orElseThrow();

        return Set.of(userRole);
    }
}
