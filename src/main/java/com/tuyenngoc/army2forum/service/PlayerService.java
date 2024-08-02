package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationSortRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPlayerInfoResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

public interface PlayerService {

    Player getPlayerById(Long playerId);

    Player updatePlayerRoles(Long playerId, Byte roleId, CustomUserDetails userDetails);

    PaginationResponseDto<GetPostResponseDto> getFollowingPosts(Long playerId, PaginationSortRequestDto requestDto);

    GetPlayerInfoResponseDto getPlayerInfo(Long playerId);

    CommonResponseDto toggleEquipmentChestLock(Long playerId);

    CommonResponseDto toggleInvitationLock(Long playerId);

}
