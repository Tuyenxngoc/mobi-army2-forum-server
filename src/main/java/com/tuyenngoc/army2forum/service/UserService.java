package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.domain.dto.PlayerDto;
import com.tuyenngoc.army2forum.domain.dto.request.ChangeUsernameRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.LockUserRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.entity.User;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

public interface UserService {

    void initAdmin(AdminInfo adminInfo);

    User getUserById(String userId);

    PlayerDto getCurrentUser(Long playerId);

    CommonResponseDto updateUserRoles(Long playerId, Byte roleId, CustomUserDetails userDetails);

    CommonResponseDto lockUserAccount(Long playerId, LockUserRequestDto requestDto, CustomUserDetails userDetails);

    CommonResponseDto changeUsername(CustomUserDetails userDetails, ChangeUsernameRequestDto requestDto);

}
