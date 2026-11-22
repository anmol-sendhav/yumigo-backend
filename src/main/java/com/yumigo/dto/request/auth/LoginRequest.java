package com.yumigo.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Email
        @NotBlank(message = "Enter a valid email address")
        String email,
        @NotBlank(message = "Enter a valid password")
        String password
) {
        @Override
        public String toString() {
                return "LoginRequest[email=" + email + "]";
        }
}
