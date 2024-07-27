package com.tuyenngoc.army2forum.domain.mapper;

import com.tuyenngoc.army2forum.domain.dto.UserDto;
import com.tuyenngoc.army2forum.domain.dto.request.auth.RegisterRequestDto;
import com.tuyenngoc.army2forum.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(RegisterRequestDto requestDto);

    @Mappings({
            @Mapping(target = "roleName", source = "role.name"),
    })
    UserDto toUserDto(User user);
}
