package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.FilePaths;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.CreateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.giftcode.GetGiftCodeDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.giftcode.GetGiftCodeResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.giftcode.GetPlayerGiftCodeResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.GetEquipmentResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.player.GetSpecialItemResponseDto;
import com.tuyenngoc.army2forum.domain.entity.GiftCode;
import com.tuyenngoc.army2forum.domain.entity.PlayerGiftCode;
import com.tuyenngoc.army2forum.domain.specification.GiftCodeSpecification;
import com.tuyenngoc.army2forum.domain.specification.PlayerGiftCodeSpecification;
import com.tuyenngoc.army2forum.exception.ConflictException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.GiftCodeRepository;
import com.tuyenngoc.army2forum.repository.PlayerGiftCodeRepository;
import com.tuyenngoc.army2forum.service.EquipRedisService;
import com.tuyenngoc.army2forum.service.GiftCodeService;
import com.tuyenngoc.army2forum.service.SpecialItemRedisService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GiftCodeServiceImpl implements GiftCodeService {

    static String tmpPath = "app/public/images/tmp";

    static String specialItemPath = "app/data/images/itemSpecial.png";

    GiftCodeRepository giftCodeRepository;

    PlayerGiftCodeRepository playerGiftCodeRepository;

    EquipRedisService equipRedisService;

    SpecialItemRedisService specialItemRedisService;

    MessageSource messageSource;

    private GiftCode getGiftCodeById(Long id) {
        return giftCodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.GiftCode.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public GetGiftCodeDetailResponseDto getGiftCodeDetailById(Long id) {
        GiftCode giftCode = getGiftCodeById(id);

        List<GetSpecialItemResponseDto> specialItemDtos = giftCode.getItems().stream()
                .map(specialItemChest -> specialItemRedisService.getSpecialItem(specialItemChest.getId())
                        .map(specialItem -> {
                            GetSpecialItemResponseDto itemResponseDto = new GetSpecialItemResponseDto(specialItem);
                            itemResponseDto.setQuantity(specialItemChest.getQuantity());
                            return itemResponseDto;
                        }).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<GetEquipmentResponseDto> equipmentDtos = giftCode.getEquips().stream()
                .map(equipChest -> equipRedisService.getEquip(equipChest.getCharacterId(), equipChest.getEquipType(), equipChest.getEquipIndex())
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

        GetGiftCodeDetailResponseDto responseDto = new GetGiftCodeDetailResponseDto(giftCode);

        responseDto.setEquips(equipmentDtos);
        responseDto.setItems(specialItemDtos);

        return responseDto;
    }

    @Override
    public PaginationResponseDto<GetGiftCodeResponseDto> getGiftCodes(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.GIFT_CODES);

        Page<GiftCode> page = giftCodeRepository.findAll(
                GiftCodeSpecification.filterGiftCodes(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable
        );

        List<GetGiftCodeResponseDto> items = page.getContent().stream()
                .map(GetGiftCodeResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.GIFT_CODES, page);

        PaginationResponseDto<GetGiftCodeResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public CommonResponseDto createGiftCode(CreateGiftCodeRequestDto requestDto) {
        boolean existsByCode = giftCodeRepository.existsByCode(requestDto.getCode());
        if (existsByCode) {
            throw new ConflictException(ErrorMessage.GiftCode.ERR_DUPLICATE_CODE, requestDto.getCode());
        }

        GiftCode giftCode = new GiftCode();
        giftCode.setCode(requestDto.getCode());
        giftCode.setUsageLimit(requestDto.getUsageLimit());
        giftCode.setExpirationDate(requestDto.getExpirationDate());
        giftCode.setXu(requestDto.getXu());
        giftCode.setLuong(requestDto.getLuong());
        giftCode.setExp(requestDto.getExp());
        giftCode.setEquips(requestDto.getEquips());
        giftCode.setItems(requestDto.getItems());

        giftCodeRepository.save(giftCode);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto updateGiftCode(Long id, UpdateGiftCodeRequestDto requestDto) {
        GiftCode giftCode = getGiftCodeById(id);

        giftCode.setUsageLimit(requestDto.getUsageLimit());
        giftCode.setExpirationDate(requestDto.getExpirationDate());

        giftCodeRepository.save(giftCode);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto deleteGiftCode(Long id) {
        GiftCode giftCode = getGiftCodeById(id);

        giftCodeRepository.delete(giftCode);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<GetPlayerGiftCodeResponseDto> getPlayersByGiftCodeId(Long id, PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.PLAYER_GIFT_CODES);

        Page<PlayerGiftCode> page = playerGiftCodeRepository.findAll(
                PlayerGiftCodeSpecification.filterByGiftCodeId(id).and(
                        PlayerGiftCodeSpecification.filterPlayerGiftCodes(requestDto.getKeyword(), requestDto.getSearchBy())
                ),
                pageable
        );

        List<GetPlayerGiftCodeResponseDto> items = page.getContent().stream()
                .map(GetPlayerGiftCodeResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.PLAYER_GIFT_CODES, page);

        PaginationResponseDto<GetPlayerGiftCodeResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

}
