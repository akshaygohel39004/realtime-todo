package com.akshay.websockettask.repository;
import com.akshay.websockettask.entity.Role;
import com.akshay.websockettask.entity.RoleTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByType(RoleTypes type);
}
