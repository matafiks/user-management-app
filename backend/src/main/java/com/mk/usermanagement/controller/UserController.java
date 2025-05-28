package com.mk.usermanagement.controller;

import com.mk.usermanagement.dto.CreateUserRequest;
import com.mk.usermanagement.dto.UpdateUserRequest;
import com.mk.usermanagement.dto.UserDto;
import com.mk.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // dodawanie użytkownika
    @Operation(summary = "Add new user")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest request) {

        userService.createUserByAdmin(request);
        return new ResponseEntity<>("New user has been created.", HttpStatus.OK);
    }

    // wyświetl użytkownika po id
    @Operation(summary = "Get user by Id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {

        UserDto user = userService.findById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // wyświetl listę użytkowników
    @Operation(summary = "Get all users")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {

        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    // edycja użytkownika
    @Operation(summary = "Update existing user")
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @Valid @RequestBody UpdateUserRequest request) {

        userService.update(userId, request);

        return new ResponseEntity<>("Successfully updated user.", HttpStatus.OK);
    }

    // usuwanie użytkownika
    @Operation(summary = "Delete user by Id")
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {

        userService.deleteById(userId);

        return new ResponseEntity<>("Successfully deleted user", HttpStatus.OK);
    }

    // pobierz użytkownika po jego username
    @Operation(summary = "Get user by username")
    @GetMapping("/by-username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {

        return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
    }

    // pobierz dane zalogowanego usera
    @Operation(summary = "Get current user info")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getCurrentUserInfo(Authentication authentication) {
        String username = authentication.getName();
        UserDto user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }


}
