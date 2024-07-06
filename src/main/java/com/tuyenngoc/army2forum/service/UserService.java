package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.domain.dto.UserDto;

public interface UserService {

    void initAdmin(AdminInfo adminInfo);

    UserDto getCurrentUser(String username);

}
