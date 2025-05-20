package com.mk.usermanagement.dto;

import jakarta.validation.constraints.*;

public record UserRegisterRequest(

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
        String password
) {
}
