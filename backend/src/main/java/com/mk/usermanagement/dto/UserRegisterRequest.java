package com.mk.usermanagement.dto;

public record UserRegisterRequest(
        String username,
        String email,
        String password
) {
}
