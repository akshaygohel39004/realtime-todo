package com.akshay.websockettask.service;

import com.akshay.websockettask.entity.type.AuthProvider;
import com.akshay.websockettask.entity.Role;
import com.akshay.websockettask.security.strategy.role.RoleStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleStrategyFactory factory;

    public Set<Role> getRoles(AuthProvider provider) {

        return factory.getStrategy(provider).getRoles();
    }
}
