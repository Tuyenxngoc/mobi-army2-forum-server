package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationSortRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.response.GetEquipmentResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetPlayerInfoResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetSpecialItemResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Equip;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Role;
import com.tuyenngoc.army2forum.domain.entity.SpecialItem;
import com.tuyenngoc.army2forum.domain.json.EquipChest;
import com.tuyenngoc.army2forum.domain.json.SpecialItemChest;
import com.tuyenngoc.army2forum.exception.ForbiddenException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.EquipRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.PostRepository;
import com.tuyenngoc.army2forum.repository.SpecialItemRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.PlayerService;
import com.tuyenngoc.army2forum.service.RoleService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import com.tuyenngoc.army2forum.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    private final PostRepository postRepository;

    private final RoleService roleService;

    private final SpecialItemRepository specialItemRepository;

    private final EquipRepository equipRepository;

    @Override
    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId));
    }

    @Override
    public Player updatePlayerRoles(Long playerId, Long roleId, CustomUserDetails userDetails) {
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

        List<GetSpecialItemResponseDto> items = new ArrayList<>();
        List<SpecialItemChest> itemChest = player.getItemChest();
        for (SpecialItemChest item : itemChest) {
            SpecialItem specialItem = specialItemRepository.findById(item.getId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, item.getId()));

            GetSpecialItemResponseDto dto = new GetSpecialItemResponseDto(specialItem);
            dto.setQuantity(item.getQuantity());
            items.add(dto);
        }

        List<GetEquipmentResponseDto> equipChest = new ArrayList<>();
        for (EquipChest equip : player.getEquipmentChest()) {
            Equip a = equipRepository.getEquip(equip.getCharacterId(), equip.getEquipType(), equip.getEquipIndex())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID));

            String imagePath = String.format("src/main/resources/static/res/bigImage/bigImage%d.png", equip.getCharacterId());
            String outputDir = "test2/";

            try {
                cutImage(imagePath, a.getBigImageCutX(), a.getBigImageCutY(), a.getBigImageSizeX(), a.getBigImageSizeY(), outputDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        GetPlayerInfoResponseDto responseDto = new GetPlayerInfoResponseDto(player);
        responseDto.setItemChest(items);

        return responseDto;
    }

    public static void cutImage(String imagePath, int[] cutX, int[] cutY, int[] sizeX, int[] sizeY, String outputDir) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(imagePath));

        for (int i = 0; i < cutX.length; i++) {
            int x = cutX[i];
            int y = cutY[i];
            int width = sizeX[i];
            int height = sizeY[i];

            BufferedImage subImage = originalImage.getSubimage(x, y, width, height);
            File outputFile = new File(outputDir + "/cut_image_" + i + ".png");
            ImageIO.write(subImage, "png", outputFile);
        }
    }

}
