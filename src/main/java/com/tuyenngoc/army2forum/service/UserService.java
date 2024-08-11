package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.domain.dto.UserDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.ChangeUsernameRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.LockUserRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateUserRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.entity.User;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

public interface UserService {

    void initAdmin(AdminInfo adminInfo);

    CommonResponseDto updateUserRoles(Long playerId, Byte roleId, CustomUserDetails userDetails);

    CommonResponseDto lockUserAccount(Long playerId, LockUserRequestDto requestDto);

    UserDto getCurrentUser(String userId);

    User updateUser(String userId, UpdateUserRequestDto requestDto);

    CommonResponseDto deleteUser(String userId);

    User getUserById(String userId);

    PaginationResponseDto<User> getUsers(PaginationFullRequestDto requestDto);

    CommonResponseDto changeUsername(CustomUserDetails userDetails, ChangeUsernameRequestDto requestDto);

}
