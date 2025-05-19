package com.mk.usermanagement.controller;

import com.mk.usermanagement.dto.UserLoginRequest;
import com.mk.usermanagement.dto.UserRegisterRequest;
import com.mk.usermanagement.entity.Role;
import com.mk.usermanagement.entity.RoleName;
import com.mk.usermanagement.entity.User;
import com.mk.usermanagement.service.JwtService;
import com.mk.usermanagement.service.RoleService;
import com.mk.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @PostMapping("/register")
    public User register(@RequestBody UserRegisterRequest userRegisterRequest) {

        Role role = roleService.findByName(String.valueOf(RoleName.ROLE_USER));

        User user = new User();
        user.setUsername(userRegisterRequest.username());
        user.setEmail(userRegisterRequest.email());
//        user.setPassword(passwordEncoder.encode(userRegisterRequest.password()));
        user.setEnabled(true);
        user.setRoles(Set.of(role));

        String encodedPassword = passwordEncoder.encode(userRegisterRequest.password());
        System.out.println("REJESTRACJA — encoded password: " + encodedPassword);
        user.setPassword(encodedPassword);


        return userService.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginRequest userLoginRequest) {

        User dbUser = userService.findByUsername(userLoginRequest.username());

        System.out.println("Hasło z formularza: " + userLoginRequest.password());
        System.out.println("Hasło z bazy: " + dbUser.getPassword());
        System.out.println("Pasuje? " + passwordEncoder.matches(userLoginRequest.password(), dbUser.getPassword()));


        if (!passwordEncoder.matches(userLoginRequest.password(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(dbUser);
    }

    @PostMapping("/debug")
    public void debug() {
        String encoded = "$2a$10$quhzMFzYhSGlXEWgb9/HL.4tjz9V71huQYyaRiLtPQ1Sczidfr5vm";

        System.out.println("test123: " + passwordEncoder.matches("test123", encoded));
        System.out.println("123456: " + passwordEncoder.matches("123456", encoded));
    }

}
