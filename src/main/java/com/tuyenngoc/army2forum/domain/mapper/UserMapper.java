package com.tuyenngoc.army2forum.domain.mapper;

import com.tuyenngoc.army2forum.domain.dto.PlayerDto;
import com.tuyenngoc.army2forum.domain.dto.UserDto;
import com.tuyenngoc.army2forum.domain.dto.request.auth.RegisterRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(RegisterRequestDto requestDto);

    @Named("mapPlayer")
    default PlayerDto getPlayerDto(Player player) {
        return new PlayerDto(player);
    }

    @Mappings({
            @Mapping(target = "roleName", source = "role.name"),
            @Mapping(target = "player", source = "player", qualifiedByName = "mapPlayer"),
    })
    UserDto toUserDto(User user);

}
