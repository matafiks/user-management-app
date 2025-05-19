package com.mk.usermanagement.service;

import com.mk.usermanagement.entity.Role;

public interface RoleService {

    Role findByName(String name);
}
