package com.mk.usermanagement.service;

import com.mk.usermanagement.dto.*;

import java.util.List;

public interface UserService {

    void register(UserRegisterRequest registerRequest);

    AuthResponseDto login(UserLoginRequest loginRequest);

    void createUserByAdmin(CreateUserRequest request);

    UserDto findById(Long id);

    List<UserDto> findAll();

    void deleteById(Long id);

    void update(Long id, UpdateUserRequest request);

    UserDto findByUsername(String username);
}
