package com.akshay.websockettask.service;


import com.akshay.websockettask.dto.authentication.jwtauth.JWTRequest;
import com.akshay.websockettask.dto.authentication.jwtauth.LoginTokens;
import com.akshay.websockettask.dto.authentication.signup.SignupRequest;
import com.akshay.websockettask.dto.authentication.signup.SignupResponse;
import com.akshay.websockettask.Exceptions.InvalidRequestException;
import com.akshay.websockettask.Exceptions.NotFoundException;
import com.akshay.websockettask.Exceptions.UserAlreadyExistsException;
import com.akshay.websockettask.entity.*;
import com.akshay.websockettask.entity.type.AuthProvider;
import com.akshay.websockettask.entity.type.RoleTypes;
import com.akshay.websockettask.mapper.SignupUserMapper;
import com.akshay.websockettask.repository.RoleRepository;
import com.akshay.websockettask.repository.UserRepository;
import com.akshay.websockettask.util.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LocalAuthService implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final SignupUserMapper signupUserMapper;

    @Override
    public SignupResponse signup(SignupRequest request) {
        validateSignupRequest(request);
        ensureUsernameNotExists(request.userName());
        Role userRole = getUserRole();
        User user = createUser(request, userRole, AuthProvider.FORM_LOGIN);
        return new SignupResponse("Signup successful", user.getUsername());
    }

    @Override
    public LoginTokens login(JWTRequest request) {
        validateLoginRequest(request);
        authenticateUser(request);
        User user = userRepository.findByUsername(request.userName()).orElseThrow(() -> new NotFoundException("User not found"));
        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        refreshTokenService.create(user, refreshToken); //for store refreshtoken inside db
        return new LoginTokens(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public LoginTokens refresh(String refreshTokenFromCookie) {
        RefreshToken oldToken = refreshTokenService.validate(refreshTokenFromCookie); //validation of token
        User user = oldToken.getUser(); //from token getUser
        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        refreshTokenService.rotate(oldToken, newRefreshToken);
        return new LoginTokens(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(String refreshTokenFromCookie) {
        RefreshToken token = refreshTokenService.validate(refreshTokenFromCookie);
        refreshTokenService.revoke(token);
    }

    private void validateSignupRequest(SignupRequest request) {
        if (request.userName() == null || request.userName().isBlank() || request.password() == null || request.password().isBlank()) {
            throw new InvalidRequestException("Username and password must not be empty");
        }
    }

    private void ensureUsernameNotExists(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }
    }

    private Role getUserRole() {
        return roleRepository.findByType(RoleTypes.ROLE_USER).orElseThrow(() -> new NotFoundException("ROLE_USER not found"));
    }

    private User createUser(SignupRequest request, Role role, AuthProvider authProvider) {
        User user =signupUserMapper.toUserEntity(request,passwordEncoder.encode(request.password()),role,authProvider);
        return userRepository.save(user);
    }

    private void validateLoginRequest(JWTRequest request) {
        if (request.userName() == null || request.password() == null) {
            throw new InvalidRequestException("Username and password are required");
        }
    }

    private void authenticateUser(JWTRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.userName(), request.password()));
    }
}
