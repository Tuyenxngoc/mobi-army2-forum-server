package com.tuyenngoc.army2forum.domain.mapper;

import com.tuyenngoc.army2forum.domain.dto.request.ClanRequestDto;
import com.tuyenngoc.army2forum.domain.entity.Clan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClanMapper {

    Clan toClan(ClanRequestDto requestDto);

}
