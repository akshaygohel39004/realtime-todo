package com.akshay.websockettask.mapper;


import com.akshay.websockettask.dto.authentication.signup.SignupRequest;
import com.akshay.websockettask.entity.type.AuthProvider;
import com.akshay.websockettask.entity.Role;
import com.akshay.websockettask.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface SignupUserMapper {

    @Mapping(target = "username", source = "request.userName")
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "roles", expression = "java(java.util.Set.of(role))")
    @Mapping(target = "authProvider", source = "authProvider")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refreshTokens", ignore = true)
    User toUserEntity(SignupRequest request, String encodedPassword, Role role, AuthProvider authProvider);
}
