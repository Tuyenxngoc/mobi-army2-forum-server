package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.CreateClanRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateClanRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanIconResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanMemberResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Clan;
import com.tuyenngoc.army2forum.domain.entity.ClanMember;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

import java.util.List;

public interface ClanService {

    CommonResponseDto createClan(CreateClanRequestDto requestDto, CustomUserDetails userDetails);

    CommonResponseDto updateClan(Long clanId, UpdateClanRequestDto requestDto, CustomUserDetails userDetails);

    CommonResponseDto deleteClan(Long clanId);

    Clan getClanById(Long clanId);

    GetClanResponseDto getClanDetailById(Long clanId, CustomUserDetails userDetails);

    PaginationResponseDto<GetClanResponseDto> getClans(PaginationFullRequestDto requestDto);

    List<GetClanIconResponseDto> getClanIcons();

    CommonResponseDto joinClan(Long clanId, CustomUserDetails userDetails);

    CommonResponseDto leaveClan(Long clanId, CustomUserDetails userDetails);

    PaginationResponseDto<GetClanMemberResponseDto> getClanMembers(Long clanId, PaginationFullRequestDto requestDto);

    PaginationResponseDto<ClanMember> getClanMembersForOwner(Long clanId, Long playerId, PaginationFullRequestDto requestDto);

}
