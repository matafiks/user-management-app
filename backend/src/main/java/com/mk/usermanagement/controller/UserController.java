package com.mk.usermanagement.controller;

import com.mk.usermanagement.dto.CreateUserRequest;
import com.mk.usermanagement.dto.UserDto;
import com.mk.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // dodawanie użytkownika
    @Operation(summary = "Add new user")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserRequest request) {

        System.out.println("CreatingUser");

        userService.createUserByAdmin(request);
        return new ResponseEntity<>("User created successfully", HttpStatus.OK);
    }

    // wyświetl użytkownika po id
    @Operation(summary = "Get user by Id")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {

        UserDto user = userService.findById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/test")
    public String testujemy() {

//        userService.createUserByAdmin();

        return "jest git";
    }

    // wyświetl listę użytkowników
    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        // TODO: implement

        return new ResponseEntity<>(List.of(), HttpStatus.OK);
    }

    // edycja użytkownika
    @Operation(summary = "Update existing user")
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId) {
        // TODO: implement

        return new ResponseEntity<>("Successfully updated user.", HttpStatus.OK);
    }

    // usuwanie użytkownika
    @Operation(summary = "Delete user by Id")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        // TODO: implement

        return new ResponseEntity<>("Successfully deleted user", HttpStatus.OK);
    }

}
