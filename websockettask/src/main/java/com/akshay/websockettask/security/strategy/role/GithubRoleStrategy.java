package com.akshay.websockettask.security.strategy.role;

import com.akshay.websockettask.entity.Role;
import com.akshay.websockettask.entity.type.RoleTypes;
import com.akshay.websockettask.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("ROLE_GITHUB")
@RequiredArgsConstructor
public class GithubRoleStrategy implements RoleStrategy {

    private final RoleRepository roleRepository;

    @Override
    public Set<Role> getRoles() {

        Role adminRole = roleRepository
                .findByType(RoleTypes.ROLE_ADMIN)
                .orElseThrow();

        return Set.of(adminRole);
    }
}
