package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationSortRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePointsRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.GetCharacterResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.GetInventoryResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.GetPlayerInfoResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.GetPointsResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

import java.util.List;

public interface PlayerService {

    Player getPlayerById(Long playerId);

    Player updatePlayerRoles(Long playerId, Byte roleId, CustomUserDetails userDetails);

    PaginationResponseDto<GetPostResponseDto> getFollowingPosts(Long playerId, PaginationSortRequestDto requestDto);

    GetPlayerInfoResponseDto getPlayerInfo(Long playerId);

    CommonResponseDto toggleEquipmentChestLock(Long playerId);

    CommonResponseDto toggleInvitationLock(Long playerId);

    GetInventoryResponseDto getPlayerInventory(Long playerId);

    GetPointsResponseDto updateAdditionalPoints(UpdatePointsRequestDto requestDto, CustomUserDetails userDetails);

    List<GetCharacterResponseDto> getPlayerCharacter(Long playerId);

    GetPointsResponseDto getPlayerPoints(Long playerId, Long id);

}
