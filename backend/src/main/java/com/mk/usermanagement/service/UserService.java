package com.mk.usermanagement.service;

import com.mk.usermanagement.entity.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    User findByUsername(String username);
}
