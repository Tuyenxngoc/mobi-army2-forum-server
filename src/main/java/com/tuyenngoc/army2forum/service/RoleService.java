package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.Role;

public interface RoleService {

    Role getRole(long roleId);

    Role getRole(String name);

    void initRoles();
}