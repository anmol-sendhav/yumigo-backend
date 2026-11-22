package com.yumigo.dto.response.auth;

public record TokenRefreshResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn
) {
    public static TokenRefreshResponse of(String accessToken, String refreshToken,
                                          long expiresInSeconds) {
        return new TokenRefreshResponse(accessToken, refreshToken, "Bearer", expiresInSeconds);
    }

    @Override
    public String toString() {
        return "TokenRefreshResponse[tokens=***]";
    }
}