package com.akshay.websockettask.security.strategy.role;

import com.akshay.websockettask.entity.Role;

import java.util.Set;

public interface RoleStrategy {

    Set<Role> getRoles();
}
