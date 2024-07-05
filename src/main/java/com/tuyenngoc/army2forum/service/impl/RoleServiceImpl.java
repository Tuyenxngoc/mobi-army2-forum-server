package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.RoleConstant;
import com.tuyenngoc.army2forum.domain.entity.Role;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.RoleRepository;
import com.tuyenngoc.army2forum.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRole(int roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Role.ERR_NOT_FOUND_ID, String.valueOf(roleId)));
    }

    @Override
    public Role getRole(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Role.ERR_NOT_FOUND_NAME, String.valueOf(name)));
    }

    @Override
    public void initRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(RoleConstant.ROLE_ADMIN.name()));
            roleRepository.save(new Role(RoleConstant.ROLE_USER.name()));
        }
    }

}
