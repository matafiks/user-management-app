package com.mk.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record CreateUserRequest(

        @NotBlank(message = "Username is required")
        @NotNull(message = "Username is required")
        String username,

        @NotBlank(message = "Email is required")
        @NotNull(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @NotNull(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = """
        Password must be at least 8 characters long and contain:
        - at least one uppercase letter
        - one lowercase letter
        - one number
        - and one special character (e.g. @, $, !, %, *, ? or &)
        """
        )
        String password,
        @NotBlank(message = "Roles are required")
        @NotNull(message = "Roles are required")
        Set<String> roles
) {
}
