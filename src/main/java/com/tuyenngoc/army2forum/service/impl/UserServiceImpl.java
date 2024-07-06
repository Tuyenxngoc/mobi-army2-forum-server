package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.RoleConstant;
import com.tuyenngoc.army2forum.domain.dto.UserDto;
import com.tuyenngoc.army2forum.domain.entity.User;
import com.tuyenngoc.army2forum.domain.mapper.UserMapper;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.UserRepository;
import com.tuyenngoc.army2forum.service.RoleService;
import com.tuyenngoc.army2forum.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void initAdmin(AdminInfo adminInfo) {
        if (userRepository.count() == 0) {
            try {
                User user = new User();
                user.setUsername(adminInfo.getUsername());
                user.setEmail(adminInfo.getEmail());
                user.setPassword(passwordEncoder.encode(adminInfo.getPassword()));
                user.setRole(roleService.getRole(RoleConstant.ROLE_ADMIN.name()));
                userRepository.save(user);

                log.info("Create admin user successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public UserDto getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME, username));
        return userMapper.toUserDto(user);
    }

}
