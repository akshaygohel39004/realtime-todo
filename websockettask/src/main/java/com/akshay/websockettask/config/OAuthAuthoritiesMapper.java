package com.akshay.websockettask.config;

import com.akshay.websockettask.entity.RoleTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class OAuthAuthoritiesMapper implements GrantedAuthoritiesMapper {

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> mapped = new HashSet<>(authorities);
        // add role as user
        mapped.add(new SimpleGrantedAuthority(RoleTypes.ROLE_USER.name()));
        return mapped;
    }
}
