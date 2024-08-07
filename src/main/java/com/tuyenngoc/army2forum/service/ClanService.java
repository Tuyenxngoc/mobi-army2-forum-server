package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.CreateClanRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateClanRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanIconResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanResponseDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

import java.util.List;

public interface ClanService {

    CommonResponseDto createClan(CreateClanRequestDto requestDto, CustomUserDetails userDetails);

    CommonResponseDto updateClan(Long clanId, UpdateClanRequestDto requestDto, CustomUserDetails userDetails);

    GetClanDetailResponseDto getClanDetailById(Long clanId);

    PaginationResponseDto<GetClanResponseDto> getClans(PaginationFullRequestDto requestDto);

    List<GetClanIconResponseDto> getClanIcons();

}
