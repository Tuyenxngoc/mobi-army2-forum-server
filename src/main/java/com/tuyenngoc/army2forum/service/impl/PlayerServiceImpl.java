package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationSortRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePointsRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.GetInventoryResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.GetPlayerInfoResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.GetSpecialItemResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Role;
import com.tuyenngoc.army2forum.exception.ForbiddenException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.PostRepository;
import com.tuyenngoc.army2forum.repository.SpecialItemRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.PlayerService;
import com.tuyenngoc.army2forum.service.RoleService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import com.tuyenngoc.army2forum.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    private final PostRepository postRepository;

    private final RoleService roleService;

    private final MessageSource messageSource;

    private final SpecialItemRepository specialItemRepository;

    @Override
    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId));
    }

    @Override
    public Player updatePlayerRoles(Long playerId, Byte roleId, CustomUserDetails userDetails) {
        Player player = getPlayerById(playerId);
        Role newRole = roleService.getRole(roleId);

        // Check if the current user's role is higher than the new role
        if (!SecurityUtils.canAssignRole(userDetails.getAuthorities(), newRole)) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN_UPDATE_DELETE);
        }

        player.getUser().setRole(newRole);
        return playerRepository.save(player);
    }

    @Override
    public PaginationResponseDto<GetPostResponseDto> getFollowingPosts(Long playerId, PaginationSortRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.POST);

        Page<GetPostResponseDto> page = postRepository.findFollowingPostsByPlayerId(playerId, pageable);
        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.POST, page);

        PaginationResponseDto<GetPostResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public GetPlayerInfoResponseDto getPlayerInfo(Long playerId) {
        Player player = getPlayerById(playerId);

        return new GetPlayerInfoResponseDto(player);
    }

    @Override
    public CommonResponseDto toggleEquipmentChestLock(Long playerId) {
        Player player = getPlayerById(playerId);
        player.setIsChestLocked(!player.getIsChestLocked());
        playerRepository.save(player);

        String message;
        if (player.getIsChestLocked()) {
            message = messageSource.getMessage(SuccessMessage.Player.CHEST_LOCKED, null, LocaleContextHolder.getLocale());
        } else {
            message = messageSource.getMessage(SuccessMessage.Player.CHEST_UNLOCKED, null, LocaleContextHolder.getLocale());
        }
        return new CommonResponseDto(message, player.getIsChestLocked());
    }

    @Override
    public CommonResponseDto toggleInvitationLock(Long playerId) {
        Player player = getPlayerById(playerId);
        player.setIsInvitationLocked(!player.getIsInvitationLocked());
        playerRepository.save(player);

        String message;
        if (player.getIsInvitationLocked()) {
            message = messageSource.getMessage(SuccessMessage.Player.INVITATION_LOCKED, null, LocaleContextHolder.getLocale());
        } else {
            message = messageSource.getMessage(SuccessMessage.Player.INVITATION_UNLOCKED, null, LocaleContextHolder.getLocale());
        }
        return new CommonResponseDto(message, player.getIsInvitationLocked());
    }

    @Override
    public GetInventoryResponseDto getPlayerInventory(Long playerId) {
        Player player = getPlayerById(playerId);

        List<GetSpecialItemResponseDto> specialItemDtos = player.getItemChest().stream()
                .map(specialItemChest -> specialItemRepository.findById(specialItemChest.getId())
                        .map(specialItem -> {
                            GetSpecialItemResponseDto itemResponseDto = new GetSpecialItemResponseDto(specialItem);
                            itemResponseDto.setQuantity(specialItemChest.getQuantity());
                            return itemResponseDto;
                        }).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        GetInventoryResponseDto responseDto = new GetInventoryResponseDto();
        responseDto.setItems(specialItemDtos);

        return responseDto;
    }

    @Override
    public Byte updateAdditionalPoints(UpdatePointsRequestDto requestDto, CustomUserDetails userDetails) {
        return null;
    }

}
