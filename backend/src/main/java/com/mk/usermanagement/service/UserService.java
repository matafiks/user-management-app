package com.mk.usermanagement.service;

import com.mk.usermanagement.dto.AuthResponseDto;
import com.mk.usermanagement.dto.UserDto;
import com.mk.usermanagement.dto.UserLoginRequest;
import com.mk.usermanagement.dto.UserRegisterRequest;
import com.mk.usermanagement.entity.User;

import java.util.List;

public interface UserService {

    void register(UserRegisterRequest registerRequest);

    AuthResponseDto login(UserLoginRequest loginRequest);

    UserDto save(User user);

    UserDto findById(Long id);

    List<UserDto> findAll();

    void deleteById(Long id);

    UserDto update(User user);
}
