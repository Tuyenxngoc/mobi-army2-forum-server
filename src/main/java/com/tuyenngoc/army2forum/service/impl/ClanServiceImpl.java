package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.ClanRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetClanResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Clan;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.mapper.ClanMapper;
import com.tuyenngoc.army2forum.domain.specification.ClanSpecification;
import com.tuyenngoc.army2forum.exception.BadRequestException;
import com.tuyenngoc.army2forum.exception.ConflictException;
import com.tuyenngoc.army2forum.exception.ForbiddenException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.ClanRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.ClanService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClanServiceImpl implements ClanService {

    @Value("${clan.creation.price}")
    private int clanCreationPrice;

    private final PlayerRepository playerRepository;

    private final ClanRepository clanRepository;

    private final ClanMapper clanMapper;

    private final MessageSource messageSource;

    @Override
    @Transactional
    public CommonResponseDto createClan(ClanRequestDto requestDto, CustomUserDetails userDetails) {
        checkDuplicateClan(requestDto);

        Player player = playerRepository.findById(userDetails.getPlayerId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, userDetails.getPlayerId()));

        if (player.getLuong() < clanCreationPrice) {
            throw new BadRequestException(ErrorMessage.Player.ERR_NOT_ENOUGH_MONEY);
        }
        player.setLuong(player.getLuong() - clanCreationPrice);
        playerRepository.save(player);

        Clan clan = clanMapper.toClan(requestDto);
        clan.setMaster(player);
        clanRepository.save(clan);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto updateClan(Long clanId, ClanRequestDto requestDto, CustomUserDetails userDetails) {
        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_FOUND_ID, clanId));

        if (!Objects.equals(clan.getMaster().getId(), userDetails.getPlayerId())) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN_UPDATE_DELETE);
        }

        checkDuplicateClan(requestDto);

        clan.setName(requestDto.getName());
        clan.setDescription(requestDto.getDescription());
        clan.setEmail(requestDto.getEmail());
        clan.setPhoneNumber(requestDto.getPhoneNumber());
        clan.setIcon(requestDto.getIcon());

        clanRepository.save(clan);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    private void checkDuplicateClan(ClanRequestDto requestDto) {
        boolean existsByName = clanRepository.existsByName(requestDto.getName());
        if (existsByName) {
            throw new ConflictException(ErrorMessage.Clan.ERR_DUPLICATE_NAME, requestDto.getName());
        }

        boolean existsByEmail = clanRepository.existsByEmail(requestDto.getEmail());
        if (existsByEmail) {
            throw new ConflictException(ErrorMessage.Clan.ERR_DUPLICATE_EMAIL, requestDto.getEmail());
        }
    }

    @Override
    public CommonResponseDto deleteClan(Long clanId, CustomUserDetails userDetails) {
        Clan clan = clanRepository.findById(clanId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_FOUND_ID, clanId));

        if (!Objects.equals(clan.getMaster().getId(), userDetails.getPlayerId())) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN_UPDATE_DELETE);
        }

        clanRepository.delete(clan);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public boolean getClanById(Long clanId) {
        Clan clan = clanRepository.findById(clanId).
                orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_FOUND_ID, clanId));
        return false;
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

}
