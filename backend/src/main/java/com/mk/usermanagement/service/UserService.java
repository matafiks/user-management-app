package com.mk.usermanagement.service;

import com.mk.usermanagement.dto.*;
import com.mk.usermanagement.entity.User;

import java.util.List;

public interface UserService {

    void register(UserRegisterRequest registerRequest);

    AuthResponseDto login(UserLoginRequest loginRequest);

    void createUserByAdmin(CreateUserRequest request);

    UserDto findById(Long id);

    List<UserDto> findAll();

    void deleteById(Long id);

    UserDto update(User user);
}
