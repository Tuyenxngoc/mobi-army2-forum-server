package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.ClanRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetClanIconResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetClanResponseDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

import java.util.List;

public interface ClanService {

    CommonResponseDto createClan(ClanRequestDto requestDto, CustomUserDetails userDetails);

    CommonResponseDto updateClan(Long clanId, ClanRequestDto requestDto, CustomUserDetails userDetails);

    CommonResponseDto deleteClan(Long clanId, CustomUserDetails userDetails);

    GetClanResponseDto getClanById(Long clanId);

    PaginationResponseDto<GetClanResponseDto> getClans(PaginationFullRequestDto requestDto);

    List<GetClanIconResponseDto> getClanIcons();

    CommonResponseDto joinClan(Long id, CustomUserDetails userDetails);

    CommonResponseDto leaveClan(Long id, CustomUserDetails userDetails);

}
