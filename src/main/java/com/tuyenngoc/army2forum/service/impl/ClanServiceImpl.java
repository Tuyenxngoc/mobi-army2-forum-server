package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.*;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.CreateClanRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateClanRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanIconResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanItemResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Clan;
import com.tuyenngoc.army2forum.domain.entity.ClanMember;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.mapper.ClanMapper;
import com.tuyenngoc.army2forum.domain.specification.ClanSpecification;
import com.tuyenngoc.army2forum.exception.BadRequestException;
import com.tuyenngoc.army2forum.exception.ConflictException;
import com.tuyenngoc.army2forum.exception.ForbiddenException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.ClanMemberRepository;
import com.tuyenngoc.army2forum.repository.ClanRepository;
import com.tuyenngoc.army2forum.repository.ClanShopRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.ClanService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClanServiceImpl implements ClanService {

    String iconsDirectory = "app/public/images/icon/clan/";

    @Value("${clan.creation.price:1000}")
    int clanCreationPrice;

    final PlayerRepository playerRepository;

    final ClanRepository clanRepository;

    final ClanMapper clanMapper;

    final MessageSource messageSource;

    final ClanMemberRepository clanMemberRepository;

    final ClanShopRepository clanShopRepository;

    private Clan getClanById(Long clanId) {
        return clanRepository.findById(clanId).
                orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_FOUND_ID, clanId));
    }

    @Override
    @Transactional
    public CommonResponseDto createClan(CreateClanRequestDto requestDto, CustomUserDetails userDetails) {
        checkDuplicateClan(requestDto.getName(), requestDto.getEmail());

        Player player = playerRepository.findById(userDetails.getPlayerId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, userDetails.getPlayerId()));

        if (player.getClanMember() != null) {
            throw new BadRequestException(ErrorMessage.Clan.ERR_ALREADY_JOINED_CLAN);
        }

        if (player.getLuong() < clanCreationPrice) {
            throw new BadRequestException(ErrorMessage.Player.ERR_NOT_ENOUGH_MONEY);
        }
        player.setLuong(player.getLuong() - clanCreationPrice);
        playerRepository.save(player);

        Clan clan = clanMapper.toClan(requestDto);
        clan.setMaster(player);
        clan.setRequireApproval(true);
        clanRepository.save(clan);

        ClanMember clanMember = new ClanMember();
        clanMember.setClan(clan);
        clanMember.setPlayer(player);
        clanMember.setRights(ClanMemberRightsConstants.CLAN_OWNER);
        clanMember.setJoinTime(LocalDateTime.now());
        clanMemberRepository.save(clanMember);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto updateClan(Long clanId, UpdateClanRequestDto requestDto, CustomUserDetails userDetails) {
        Clan clan = getClanById(clanId);

        if (!Objects.equals(clan.getMaster().getId(), userDetails.getPlayerId())) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN_UPDATE_DELETE);
        }

        clan.setDescription(requestDto.getDescription());
        clan.setNotification(requestDto.getNotification());
        clan.setRequireApproval(requestDto.getRequireApproval());
        clan.setIcon(requestDto.getIcon());

        clanRepository.save(clan);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    private void checkDuplicateClan(String name, String email) {
        boolean existsByName = clanRepository.existsByName(name);
        if (existsByName) {
            throw new ConflictException(ErrorMessage.Clan.ERR_DUPLICATE_NAME, name);
        }

        boolean existsByEmail = clanRepository.existsByEmail(email);
        if (existsByEmail) {
            throw new ConflictException(ErrorMessage.Clan.ERR_DUPLICATE_EMAIL, email);
        }
    }

    @Override
    public GetClanDetailResponseDto getClanDetailById(Long clanId) {
        Clan clan = getClanById(clanId);
        GetClanDetailResponseDto responseDto = new GetClanDetailResponseDto(clan);

        LocalDateTime currentTime = LocalDateTime.now();

        List<GetClanItemResponseDto> items = clan.getItems().stream()
                .map(clanItem -> clanShopRepository.findById(clanItem.getId())
                        .map(clanShop -> {
                            GetClanItemResponseDto dto = new GetClanItemResponseDto();
                            Duration duration = Duration.between(currentTime, clanItem.getTime());
                            dto.setTime(duration.getSeconds());
                            dto.setName(clanShop.getName());
                            return dto;
                        }).orElse(null)
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        responseDto.setItems(items);

        return responseDto;
    }

    @Override
    public PaginationResponseDto<GetClanResponseDto> getClans(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.CLAN);

        Page<Clan> page = clanRepository.findAll(
                ClanSpecification.filterClans(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable
        );

        List<GetClanResponseDto> items = page.getContent().stream()
                .map(GetClanResponseDto::new)
                .collect(Collectors.toList());

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.CLAN, page);

        PaginationResponseDto<GetClanResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public List<GetClanIconResponseDto> getClanIcons() {
        try {
            File directory = new File(iconsDirectory);
            if (!directory.exists()) {
                return List.of();
            }
            if (!directory.isDirectory()) {
                return List.of();
            }

            return Files.list(directory.toPath())
                    .filter(Files::isRegularFile)
                    .map(filePath -> {
                        try {
                            String fileName = filePath.getFileName().toString();
                            Long id = Long.parseLong(fileName.split("\\.")[0]);
                            String src = FilePaths.ICON_CLAN_PATH + fileName;
                            return new GetClanIconResponseDto(id, src);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing ID from file: " + filePath + ", Exception: " + e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

}
