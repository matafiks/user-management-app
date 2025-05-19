package com.mk.usermanagement.dto;

public record UserLoginRequest(
        String username,
        String password
) {
}
