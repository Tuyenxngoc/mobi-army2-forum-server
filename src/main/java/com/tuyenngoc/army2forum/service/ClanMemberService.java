package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanMemberDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanMemberResponseDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

public interface ClanMemberService {

    CommonResponseDto joinClan(Long clanId, CustomUserDetails userDetails);

    CommonResponseDto leaveClan(Long clanId, CustomUserDetails userDetails);

    PaginationResponseDto<GetClanMemberResponseDto> getClanMembers(Long clanId, PaginationFullRequestDto requestDto);

    PaginationResponseDto<GetClanMemberDetailResponseDto> getClanMembersForOwner(Long clanId, Long playerId, PaginationFullRequestDto requestDto);

    CommonResponseDto removeMember(Long clanId, Long memberId, Long playerId);

    CommonResponseDto promoteMember(Long clanId, Long memberId, Long playerId);

}
