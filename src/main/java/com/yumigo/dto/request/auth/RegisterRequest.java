package com.yumigo.dto.request.auth;

import com.yumigo.enums.Role;
import jakarta.validation.constraints.*;

public record RegisterRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 150, message = "Email must be at most 150 characters")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit mobile number")
        String phone,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,64}$",
                message = "Password must be 8-64 characters with an uppercase letter, a lowercase letter, a digit and a special character")
        String password,

        @NotNull(message = "Role is required")
        Role role
) {
    @Override
    public String toString() {
        return "RegisterRequest[name=" + name + ", email=" + email
                + ", phone=" + phone + ", role=" + role + "]";
    }
}