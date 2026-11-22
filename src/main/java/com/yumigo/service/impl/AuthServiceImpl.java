package com.yumigo.service.impl;

import com.yumigo.dto.request.auth.LoginRequest;
import com.yumigo.dto.request.auth.RefreshTokenRequest;
import com.yumigo.dto.request.auth.RegisterRequest;
import com.yumigo.dto.response.auth.AuthResponse;
import com.yumigo.dto.response.auth.TokenRefreshResponse;
import com.yumigo.dto.response.user.UserResponse;
import com.yumigo.entity.RefreshToken;
import com.yumigo.entity.User;
import com.yumigo.enums.Role;
import com.yumigo.enums.UserStatus;
import com.yumigo.exception.BadRequestException;
import com.yumigo.exception.DuplicateResourceException;
import com.yumigo.exception.ForbiddenException;
import com.yumigo.exception.UnauthorizedException;
import com.yumigo.mapper.UserMapper;
import com.yumigo.repository.UserRepository;
import com.yumigo.security.CustomUserDetails;
import com.yumigo.security.JwtService;
import com.yumigo.service.AuthService;
import com.yumigo.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {

        if (request.role() == Role.ADMIN) {
            throw new BadRequestException("Admin Account cannot be self Registered");
        }

        String email = request.email().trim().toLowerCase(Locale.ROOT);
        String phone = request.phone().trim();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email is already registered");
        }

        if (userRepository.existsByPhone(phone)) {
            throw new DuplicateResourceException("Phone number is already registered");
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setEmailVerified(false);
        user.setStatus(request.role() == Role.CUSTOMER
                ? UserStatus.ACTIVE
                : UserStatus.PENDING_APPROVAL);

        try {
            return userMapper.toUserResponse(userRepository.saveAndFlush(user));
        } catch (DataIntegrityViolationException e) {
            // two simultaneous requests passed the exists-checks; the DB unique constraint caught it
            throw new DuplicateResourceException("Email or phone is already registered");
        }
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.password()));
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid email or password");
        } catch (LockedException | DisabledException e) {
            throw new ForbiddenException("This account is not allowed to sign in");
        }

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        String accessToken = jwtService.generateAccessToken(principal);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer",
                jwtService.getAccessTokenExpirySeconds(),
                userMapper.toUserResponse(user));
    }

    @Override
    public TokenRefreshResponse refresh(RefreshTokenRequest request) {
        RefreshToken newToken = refreshTokenService.rotate(request.refreshToken());
        String accessToken = jwtService.generateAccessToken(new CustomUserDetails(newToken.getUser()));
        return new TokenRefreshResponse(accessToken, newToken.getToken(), "Bearer",
                jwtService.getAccessTokenExpirySeconds());
    }

    @Override
    public void logout(RefreshTokenRequest request, Long userId) {
        refreshTokenService.revoke(request.refreshToken(), userId);
    }
}
