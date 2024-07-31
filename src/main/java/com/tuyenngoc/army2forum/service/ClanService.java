package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.ClanRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetClanResponseDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

public interface ClanService {

    CommonResponseDto createClan(ClanRequestDto requestDto, CustomUserDetails userDetails);

    CommonResponseDto updateClan(Long clanId, ClanRequestDto requestDto, CustomUserDetails userDetails);

    CommonResponseDto deleteClan(Long clanId, CustomUserDetails userDetails);

    boolean getClanById(Long clanId);

    PaginationResponseDto<GetClanResponseDto> getClans(PaginationFullRequestDto requestDto);

}
