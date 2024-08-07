package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ClanMemberRightsConstants;
import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.CreateClanRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateClanRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanIconResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanItemResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanMemberResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Clan;
import com.tuyenngoc.army2forum.domain.entity.ClanApproval;
import com.tuyenngoc.army2forum.domain.entity.ClanMember;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.mapper.ClanMapper;
import com.tuyenngoc.army2forum.domain.specification.ClanSpecification;
import com.tuyenngoc.army2forum.exception.BadRequestException;
import com.tuyenngoc.army2forum.exception.ConflictException;
import com.tuyenngoc.army2forum.exception.ForbiddenException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.*;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.ClanService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClanServiceImpl implements ClanService {

    @Value("${app.icons-directory}")
    private String iconsDirectory;

    @Value("${clan.creation.price}")
    private int clanCreationPrice;

    private final PlayerRepository playerRepository;

    private final ClanRepository clanRepository;

    private final ClanMapper clanMapper;

    private final MessageSource messageSource;

    private final ClanMemberRepository clanMemberRepository;

    private final ClanApprovalRepository clanApprovalRepository;

    private final ClanShopRepository clanShopRepository;

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
    public CommonResponseDto deleteClan(Long clanId) {
        clanRepository.deleteById(clanId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public Clan getClanById(Long clanId) {
        return clanRepository.findById(clanId).
                orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_FOUND_ID, clanId));
    }

    @Override
    public GetClanResponseDto getClanDetailById(Long clanId, CustomUserDetails userDetails) {
        Clan clan = getClanById(clanId);
        GetClanResponseDto responseDto = new GetClanResponseDto(clan);

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
            // Get the path to the resource directory
            Resource resource = new ClassPathResource(iconsDirectory);
            Path path = Paths.get(resource.getURI());

            // List all files in the directory
            return Files.list(path)
                    .filter(Files::isRegularFile)
                    .map(filePath -> {
                        try {
                            String fileName = filePath.getFileName().toString();
                            Long id = Long.parseLong(fileName.split("\\.")[0]); // Extract ID from filename
                            String src = "/res/icon/clan/" + fileName; // Relative URL to serve static content
                            return new GetClanIconResponseDto(id, src);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing ID from file: " + filePath + ", Exception: " + e.getMessage());
                            return null; // Skip this file if parsing fails
                        }
                    })
                    .filter(Objects::nonNull) // Filter out any null results
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public CommonResponseDto joinClan(Long clanId, CustomUserDetails userDetails) {
        Clan clan = getClanById(clanId);

        if (clan.getMembers().size() >= clan.getMemMax()) {
            throw new BadRequestException(ErrorMessage.Clan.ERR_CLAN_IS_FULL);
        }

        Player player = playerRepository.findById(userDetails.getPlayerId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, userDetails.getPlayerId()));

        if (player.getClanMember() != null) {
            throw new BadRequestException(ErrorMessage.Clan.ERR_ALREADY_JOINED_CLAN);
        }

        if (clan.getRequireApproval()) {
            boolean exists = clanApprovalRepository.existsByClanIdAndPlayerId(clanId, userDetails.getPlayerId());
            if (exists) {
                throw new ConflictException(ErrorMessage.Clan.ERR_ALREADY_REQUESTED_JOIN);
            }

            ClanApproval clanApproval = new ClanApproval();
            clanApproval.setClan(clan);
            clanApproval.setPlayer(player);
            clanApprovalRepository.save(clanApproval);

            String message = messageSource.getMessage(SuccessMessage.Clan.REQUEST_SUBMITTED, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(message);
        } else {
            clanApprovalRepository.removeAllByPlayerId(userDetails.getPlayerId());

            ClanMember clanMember = new ClanMember();
            clanMember.setClan(clan);
            clanMember.setPlayer(player);
            clanMember.setRights(ClanMemberRightsConstants.CLAN_MEMBER);
            clanMember.setJoinTime(LocalDateTime.now());
            clanMemberRepository.save(clanMember);

            String message = messageSource.getMessage(SuccessMessage.Clan.JOINED_SUCCESSFULLY, null, LocaleContextHolder.getLocale());
            return new CommonResponseDto(message);
        }
    }

    @Override
    public CommonResponseDto leaveClan(Long clanId, CustomUserDetails userDetails) {
        ClanMember clanMember = clanMemberRepository.findByClanIdAndPlayerId(clanId, userDetails.getPlayerId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_JOINED_CLAN));

        if (clanMember.getRights() == 2) {//Owner clan
            throw new BadRequestException(ErrorMessage.Clan.ERR_OWNER_CANNOT_LEAVE);
        } else {
            clanMemberRepository.delete(clanMember);
        }

        String message = messageSource.getMessage(SuccessMessage.Clan.LEAVE_SUCCESS, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<GetClanMemberResponseDto> getClanMembers(Long clanId, PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.CLAN_MEMBER);

        Page<ClanMember> page = clanMemberRepository.findAll(
                ClanSpecification.getClanMembersByClanId(clanId).and(
                        ClanSpecification.filterClanMembers(requestDto.getKeyword(), requestDto.getSearchBy())),
                pageable
        );

        List<GetClanMemberResponseDto> items = page.getContent().stream()
                .map(GetClanMemberResponseDto::new)
                .collect(Collectors.toList());

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.CLAN_MEMBER, page);

        PaginationResponseDto<GetClanMemberResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public PaginationResponseDto<ClanMember> getClanMembersForOwner(Long clanId, Long playerId, PaginationFullRequestDto requestDto) {
        Clan clan = getClanById(clanId);
        if (!Objects.equals(clan.getMaster().getId(), playerId)) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN);
        }

        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.CLAN_MEMBER);

        Page<ClanMember> page = clanMemberRepository.findAll(
                ClanSpecification.getClanMembersByClanId(clanId).and(
                        ClanSpecification.filterClanMembers(requestDto.getKeyword(), requestDto.getSearchBy())),
                pageable
        );

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.CLAN_MEMBER, page);

        PaginationResponseDto<ClanMember> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

}
