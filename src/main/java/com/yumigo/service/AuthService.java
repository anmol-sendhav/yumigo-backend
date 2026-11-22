package com.yumigo.service;

import com.yumigo.dto.request.auth.LoginRequest;
import com.yumigo.dto.request.auth.RefreshTokenRequest;
import com.yumigo.dto.request.auth.RegisterRequest;
import com.yumigo.dto.response.auth.AuthResponse;
import com.yumigo.dto.response.auth.TokenRefreshResponse;
import com.yumigo.dto.response.user.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    public TokenRefreshResponse refresh(RefreshTokenRequest request);

    public void logout(RefreshTokenRequest request, Long userId);
}
