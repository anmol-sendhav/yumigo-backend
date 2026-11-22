package com.yumigo.dto.response.auth;

import com.yumigo.dto.response.user.UserResponse;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        UserResponse user
) {
    public static AuthResponse of(String accessToken, String refreshToken,
                                  long expiresInSeconds, UserResponse user) {
        return new AuthResponse(accessToken, refreshToken, "Bearer", expiresInSeconds, user);
    }

    @Override
    public String toString() {
        return "AuthResponse[user=" + user + ", tokens=***]";
    }
}