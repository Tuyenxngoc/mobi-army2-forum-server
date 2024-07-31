package com.tuyenngoc.army2forum.domain.mapper;

import com.tuyenngoc.army2forum.domain.dto.ClanDto;
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

    @Named("mapClan")
    default ClanDto getClanDto(Player player) {
        if (player.getClanMember() == null) {
            return null;
        }

        return new ClanDto(player.getClanMember().getClan());
    }

    @Mappings({
            @Mapping(target = "roleName", source = "role.name"),
            @Mapping(target = "player", source = "player", qualifiedByName = "mapPlayer"),
            @Mapping(target = "clan", source = "player", qualifiedByName = "mapClan"),
    })
    UserDto toUserDto(User user);

}
