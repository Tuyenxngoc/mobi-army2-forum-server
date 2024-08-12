package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.Role;

import java.util.List;

public interface RoleService {

    Role getRole(byte roleId);

    Role getRole(String name);

    void initRoles();

    List<Role> getRoles();

}