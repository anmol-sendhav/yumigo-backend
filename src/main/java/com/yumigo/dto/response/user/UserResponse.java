package com.yumigo.dto.response.user;

import com.yumigo.enums.Role;
import com.yumigo.enums.UserStatus;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        String phone,
        Role role,
        UserStatus status,
        boolean emailVerified,
        LocalDateTime createdA
) {
}
