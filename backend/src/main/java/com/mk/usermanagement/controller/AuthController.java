package com.mk.usermanagement.controller;

import com.mk.usermanagement.dto.AuthResponseDto;
import com.mk.usermanagement.dto.UserLoginRequest;
import com.mk.usermanagement.dto.UserRegisterRequest;
import com.mk.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterRequest registerRequest) {

        userService.register(registerRequest);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }

    @Operation(summary = "Login into an existing account")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody UserLoginRequest loginRequest) {

        AuthResponseDto dto = userService.login(loginRequest);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
