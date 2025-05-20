package com.mk.usermanagement.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDto {

    private String token;
    private String username;
    private Set<String> roles;
}
