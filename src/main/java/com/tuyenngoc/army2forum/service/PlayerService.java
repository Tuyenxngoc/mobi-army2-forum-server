package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationSortRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePointsRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.*;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

import java.util.List;

public interface PlayerService {

    PaginationResponseDto<GetPostResponseDto> getFollowingPosts(Long playerId, PaginationSortRequestDto requestDto);

    CommonResponseDto toggleEquipmentChestLock(Long playerId);

    CommonResponseDto toggleInvitationLock(Long playerId);

    GetInventoryResponseDto getPlayerInventory(Long playerId);

    GetPointsResponseDto updateAdditionalPoints(UpdatePointsRequestDto requestDto, CustomUserDetails userDetails);

    List<GetCharacterResponseDto> getPlayerCharacter(Long playerId);

    GetPointsResponseDto getPlayerPoints(Long playerId, Long id);

    GetPlayerInfoResponseDto getPlayerInfoById(Long playerId, Long playerIdRequest);

    PaginationResponseDto<GetPlayerResponseDto> getPlayers(PaginationFullRequestDto requestDto);

    String getPlayerAvatar(Long playerId);

}
