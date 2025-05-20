package com.mk.usermanagement.controller;

import com.mk.usermanagement.dto.UserDto;
import com.mk.usermanagement.entity.User;
import com.mk.usermanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // dodawanie użytkownika
    @Operation(summary = "Add new user")
    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user) {
        // TODO: imlement
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    // wyświetl użytkownika po id
    @Operation(summary = "Get user by Id")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        // TODO: imlement

        return new ResponseEntity<>(new UserDto(), HttpStatus.OK);
    }

    // wyświetl listę użytkowników
    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        // TODO: imlement

        return new ResponseEntity<>(List.of(), HttpStatus.OK);
    }

    // edycja użytkownika
    @Operation(summary = "Update existing user")
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId) {
        // TODO: imlement

        return new ResponseEntity<>("Successfully updated user.", HttpStatus.OK);
    }

    // usuwanie użytkownika
    @Operation(summary = "Delete user by Id")
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        // TODO: imlement

        return new ResponseEntity<>("Successfully deleted user", HttpStatus.OK);
    }

}
