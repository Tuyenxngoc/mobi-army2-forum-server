package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.FilePaths;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationSortRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.UpdatePointsRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.*;
import com.tuyenngoc.army2forum.domain.dto.response.post.GetPostResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Equip;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.PlayerCharacters;
import com.tuyenngoc.army2forum.domain.json.EquipChest;
import com.tuyenngoc.army2forum.domain.specification.PlayerSpecification;
import com.tuyenngoc.army2forum.exception.BadRequestException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.*;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.PlayerService;
import com.tuyenngoc.army2forum.util.GifCreator;
import com.tuyenngoc.army2forum.util.MaskingUtils;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerServiceImpl implements PlayerService {

    static final int[][] HEAD_1 = {
            {13, 10, 0, 96, 24, 24},
            {13, 10, 0, 96, 24, 24},
            {13, 10, 0, 96, 24, 24},
            {13, 16, 0, 134, 30, 30},
            {13, 10, 0, 96, 24, 24},
            {13, 10, 0, 96, 24, 24},
            {13, 10, 0, 96, 24, 24},
            {13, 14, 0, 132, 32, 32},
            {13, 10, 0, 96, 24, 24},
            {13, 10, 0, 96, 24, 24}
    };

    static final int[][] HEAD_2 = {
            {13, 10, 0, 120, 24, 24},
            {13, 10, 0, 120, 24, 24},
            {13, 10, 0, 120, 24, 24},
            {13, 15, 0, 165, 30, 30},
            {13, 10, 0, 120, 24, 24},
            {13, 10, 0, 120, 24, 24},
            {13, 11, 0, 120, 24, 24},
            {13, 14, 0, 164, 32, 32},
            {13, 10, 0, 120, 24, 24},
            {13, 10, 0, 120, 24, 24}
    };

    private final String tmpPath = "app/public/images/tmp";

    private final String avatarPath = "app/public/images/avatar/%s_%d.gif";

    private final String specialItemPath = "app/data/images/itemSpecial.png";

    private final String bigImagePath = "app/data/images/bigImage/bigImage%d.png";

    private final String playerPath = "app/data/images/player/%d.png";

    final PlayerRepository playerRepository;

    final PostRepository postRepository;

    final MessageSource messageSource;

    final SpecialItemRepository specialItemRepository;

    final EquipRepository equipRepository;

    final PlayerCharacterRepository playerCharacterRepository;

    private Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId));
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

        List<GetEquipmentResponseDto> equipmentDtos = player.getEquipmentChest().stream()
                .map(equipChest -> equipRepository.getEquip(equipChest.getCharacterId(), equipChest.getEquipType(), equipChest.getEquipIndex())
                        .map(equip -> {
                            GetEquipmentResponseDto equipResponseDto = new GetEquipmentResponseDto(equip, equipChest);

                            // Kiểm tra và tạo thư mục tmp nếu chưa tồn tại
                            File tmpDir = new File(tmpPath);
                            if (!tmpDir.exists()) {
                                tmpDir.mkdirs();
                            }

                            // Tên ảnh được lưu trong thư mục tmp
                            String frameCountImageName = equip.getFrameCount() + ".png";
                            File frameCountImageFile = new File(tmpDir, frameCountImageName);

                            // Kiểm tra xem ảnh đã tồn tại trong thư mục tmp chưa
                            if (!frameCountImageFile.exists()) {
                                try {
                                    // Đọc ảnh gốc
                                    BufferedImage originalImage = ImageIO.read(new File(specialItemPath));

                                    // Cắt ảnh từ ảnh gốc
                                    int y = equip.getFrameCount() * 16;
                                    BufferedImage subImage = originalImage.getSubimage(0, y, 16, 16);

                                    // Lưu ảnh đã cắt vào thư mục tmp
                                    ImageIO.write(subImage, "png", frameCountImageFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            }

                            // Tạo link ảnh
                            equipResponseDto.setImageUrl(FilePaths.TMP_PATH + frameCountImageName);

                            return equipResponseDto;
                        }).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        GetInventoryResponseDto responseDto = new GetInventoryResponseDto();
        responseDto.setEquipments(equipmentDtos);
        responseDto.setItems(specialItemDtos);

        return responseDto;
    }

    @Override
    public GetPointsResponseDto updateAdditionalPoints(UpdatePointsRequestDto requestDto, CustomUserDetails userDetails) {
        int totalPoints = requestDto.getHealth()
                + requestDto.getDamage()
                + requestDto.getDefense()
                + requestDto.getLuck()
                + requestDto.getTeammates();

        if (totalPoints == 0) {
            throw new BadRequestException(ErrorMessage.Player.ERR_POINTS_INVALID);
        }

        PlayerCharacters playerCharacters = playerCharacterRepository.findByIdAndPlayerId(requestDto.getPlayerCharacterId(), userDetails.getPlayerId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_CHARACTER_ID, requestDto.getPlayerCharacterId()));

        if (playerCharacters.getPlayer().getIsOnline()) {
            throw new BadRequestException(ErrorMessage.Player.ERR_PLAYER_IS_ONLINE);
        }

        if (totalPoints > playerCharacters.getPoints()) {
            throw new BadRequestException(ErrorMessage.Player.ERR_POINTS_EXCEEDED, playerCharacters.getPoints());
        }

        playerCharacters.setPoints(playerCharacters.getPoints() - totalPoints);

        int[] additionalPoints = playerCharacters.getAdditionalPoints();
        additionalPoints[0] += requestDto.getHealth();
        additionalPoints[1] += requestDto.getDamage();
        additionalPoints[2] += requestDto.getDefense();
        additionalPoints[3] += requestDto.getLuck();
        additionalPoints[4] += requestDto.getTeammates();

        playerCharacters.setAdditionalPoints(additionalPoints);

        playerCharacterRepository.save(playerCharacters);

        return new GetPointsResponseDto(additionalPoints, playerCharacters.getPoints());
    }

    @Override
    public List<GetCharacterResponseDto> getPlayerCharacter(Long playerId) {
        return playerCharacterRepository.getByPlayerId(playerId);
    }

    @Override
    public GetPointsResponseDto getPlayerPoints(Long playerId, Long id) {
        PlayerCharacters playerCharacters = playerCharacterRepository.findByIdAndPlayerId(id, playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_CHARACTER_ID, id));

        return new GetPointsResponseDto(playerCharacters.getAdditionalPoints(), playerCharacters.getPoints());
    }

    @Override
    public GetPlayerInfoResponseDto getPlayerInfoById(Long playerId, Long playerIdRequest) {
        Player player = getPlayerById(playerId);

        GetPlayerInfoResponseDto responseDto = new GetPlayerInfoResponseDto(player);
        if (playerId.equals(playerIdRequest)) {
            responseDto.setX2XpTime(player.getX2XpTime());
            responseDto.setEmail(MaskingUtils.maskEmail(player.getUser().getEmail()));
            responseDto.setPhoneNumber(MaskingUtils.maskPhoneNumber(player.getUser().getPhoneNumber()));
            responseDto.setIsChestLocked(player.getIsChestLocked());
            responseDto.setIsInvitationLocked(player.getIsInvitationLocked());

        }

        return responseDto;
    }

    @Override
    public PaginationResponseDto<GetPlayerResponseDto> getPlayers(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.PLAYER);

        Page<Player> page = playerRepository.findAll(
                PlayerSpecification.filterPlayers(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable
        );

        List<GetPlayerResponseDto> items = page.getContent().stream()
                .map(GetPlayerResponseDto::new)
                .collect(Collectors.toList());

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.PLAYER, page);

        PaginationResponseDto<GetPlayerResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public void getPlayerAvatar(Long playerId) {
        Player player = getPlayerById(playerId);
        int[] data = player.getActiveCharacter().getData();
        String username = player.getUser().getUsername();
        byte characterId = player.getActiveCharacter().getCharacter().getId();

        LocalDateTime now = LocalDateTime.now();
        List<Equip> equips = new ArrayList<>();
        boolean isDisguiseSet = handleDisguise(data, player, characterId, now, equips);

        if (!isDisguiseSet) {
            handleEquipments(data, player, now, equips);
            addDefaultEquips(characterId, equips);
        }

        createAvatarImage(characterId, username, equips);
    }

    private boolean handleDisguise(int[] data, Player player, byte characterId, LocalDateTime now, List<Equip> equips) {
        if (data[5] != -1) {
            EquipChest equipChest = findEquipChest(data[5], player.getEquipmentChest());
            if (equipChest != null) {
                Equip equip = getValidEquip(equipChest, now);
                if (equip != null && equip.getIsDisguise() == 1) {
                    List<Integer> disguiseIndexes = Arrays.stream(equip.getDisguiseEquippedIndexes())
                            .boxed()
                            .collect(Collectors.toList());
                    equips.addAll(equipRepository.getEquipByIndexes(characterId, disguiseIndexes));
                    return true;
                }
            }
        }
        return false;
    }

    private void handleEquipments(int[] data, Player player, LocalDateTime now, List<Equip> equips) {
        List<EquipChest> equipChests = Arrays.stream(data)
                .filter(key -> key != -1)
                .mapToObj(key -> findEquipChest(key, player.getEquipmentChest()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (EquipChest equipChest : equipChests) {
            Equip equip = getValidEquip(equipChest, now);
            if (equip != null) {
                equips.add(equip);
            }
        }
    }

    private EquipChest findEquipChest(int key, List<EquipChest> equipmentChest) {
        int index = equipmentChest.indexOf(new EquipChest(key));
        return index != -1 ? equipmentChest.get(index) : null;
    }

    private Equip getValidEquip(EquipChest equipChest, LocalDateTime now) {
        return equipRepository.getEquip(equipChest.getCharacterId(), equipChest.getEquipType(), equipChest.getEquipIndex())
                .filter(equip -> ChronoUnit.DAYS.between(equipChest.getPurchaseDate(), now) <= equip.getExpirationDays())
                .orElse(null);
    }

    private void addDefaultEquips(byte characterId, List<Equip> equips) {
        List<Equip> defaultEquips = equipRepository.getEquipDefault(characterId);
        for (Equip defaultEquip : defaultEquips) {
            if (equips.stream().noneMatch(equip -> equip.getEquipType().equals(defaultEquip.getEquipType()))) {
                equips.add(defaultEquip);
            }
        }
    }

    private void createAvatarImage(byte characterId, String username, List<Equip> equips) {
        try {
            BufferedImage bigImage = ImageIO.read(new File(String.format(bigImagePath, characterId)));
            BufferedImage playerImage = ImageIO.read(new File(String.format(playerPath, characterId)));

            BufferedImage image1 = createImage(bigImage, playerImage, HEAD_1[characterId], equips, 4);
            BufferedImage image2 = createImage(bigImage, playerImage, HEAD_2[characterId], equips, 5);

            String outputGifPath = String.format(avatarPath, username, characterId);
            GifCreator.createGif(image1, image2, outputGifPath);
        } catch (IOException e) {
            log.error("Error creating GIF image", e);
        }
    }

    private BufferedImage createImage(BufferedImage bigImage, BufferedImage playerImage, int[] cutCoordinates, List<Equip> equips, int index) {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        for (Equip equip : equips) {
            if (equip.getEquipType() == 0 || equip.getEquipType() == 4) {
                if (!isRegionNotDefined(equip, index)) {
                    BufferedImage equipImage = bigImage.getSubimage(
                            equip.getBigImageCutX()[index],
                            equip.getBigImageCutY()[index],
                            equip.getBigImageSizeX()[index],
                            equip.getBigImageSizeY()[index]
                    );
                    graphics.drawImage(equipImage, 31 + equip.getBigImageAlignX()[index], 50 + equip.getBigImageAlignY()[index], null);
                }
            }
        }

        BufferedImage playerImageCut = playerImage.getSubimage(cutCoordinates[2], cutCoordinates[3], cutCoordinates[4], cutCoordinates[5]);
        graphics.drawImage(playerImageCut, cutCoordinates[0], cutCoordinates[1], null);

        for (Equip equip : equips) {
            if (equip.getEquipType() == 0 || equip.getEquipType() == 4) {
                continue;
            }
            if (!isRegionNotDefined(equip, index)) {
                BufferedImage equipImage = bigImage.getSubimage(
                        equip.getBigImageCutX()[index],
                        equip.getBigImageCutY()[index],
                        equip.getBigImageSizeX()[index],
                        equip.getBigImageSizeY()[index]
                );
                graphics.drawImage(equipImage, 31 + equip.getBigImageAlignX()[index], 50 + equip.getBigImageAlignY()[index], null);
            }
        }

        graphics.dispose();

        return image;
    }

    private boolean isRegionNotDefined(Equip equip, int index) {
        return equip.getBigImageCutX()[index] == 0 &&
                equip.getBigImageCutY()[index] == 0 &&
                equip.getBigImageSizeX()[index] == 0 &&
                equip.getBigImageSizeY()[index] == 0;
    }

    public static String getAvatar(String playerName, long characterId) {
        String avatarPath = String.format("/images/avatar/%s_%d.gif", playerName, characterId);

        if (Files.exists(Paths.get("app/public" + avatarPath))) {
            return avatarPath;
        } else {
            return "/images/default.png";
        }
    }

}