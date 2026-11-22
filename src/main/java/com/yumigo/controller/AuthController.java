package com.yumigo.controller;

import com.yumigo.dto.request.auth.LoginRequest;
import com.yumigo.dto.request.auth.RefreshTokenRequest;
import com.yumigo.dto.request.auth.RegisterRequest;
import com.yumigo.dto.response.auth.AuthResponse;
import com.yumigo.dto.response.auth.TokenRefreshResponse;
import com.yumigo.dto.response.user.UserResponse;
import com.yumigo.security.CustomUserDetails;
import com.yumigo.service.AuthService;
import com.yumigo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

@PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid  @RequestBody RegisterRequest request){
    UserResponse response = authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

@PostMapping("/login")
    public ResponseEntity<AuthResponse>login(@Valid @RequestBody LoginRequest request){
    return ResponseEntity.ok(authService.login(request));
}

@GetMapping("/users/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal CustomUserDetails principal){
    return ResponseEntity.ok(userService.getCurrentUser(principal.getId()));
}

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request,
                                       @AuthenticationPrincipal CustomUserDetails principal) {
        authService.logout(request, principal.getId());
        return ResponseEntity.noContent().build();   // 204
    }
}
