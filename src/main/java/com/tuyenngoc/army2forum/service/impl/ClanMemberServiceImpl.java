package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ClanMemberRightsConstants;
import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanMemberDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.clan.GetClanMemberResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Clan;
import com.tuyenngoc.army2forum.domain.entity.ClanApproval;
import com.tuyenngoc.army2forum.domain.entity.ClanMember;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.specification.ClanSpecification;
import com.tuyenngoc.army2forum.exception.BadRequestException;
import com.tuyenngoc.army2forum.exception.ConflictException;
import com.tuyenngoc.army2forum.exception.ForbiddenException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.ClanApprovalRepository;
import com.tuyenngoc.army2forum.repository.ClanMemberRepository;
import com.tuyenngoc.army2forum.repository.ClanRepository;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.ClanMemberService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClanMemberServiceImpl implements ClanMemberService {

    PlayerRepository playerRepository;

    ClanRepository clanRepository;

    MessageSource messageSource;

    ClanMemberRepository clanMemberRepository;

    ClanApprovalRepository clanApprovalRepository;

    @Override
    public CommonResponseDto joinClan(Long clanId, CustomUserDetails userDetails) {
        Clan clan = clanRepository.findById(clanId).
                orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_FOUND_ID, clanId));

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
    public PaginationResponseDto<GetClanMemberDetailResponseDto> getClanMembersForOwner(Long clanId, Long playerId, PaginationFullRequestDto requestDto) {
        Clan clan = clanRepository.findById(clanId).
                orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_FOUND_ID, clanId));
        if (!Objects.equals(clan.getMaster().getId(), playerId)) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN);
        }

        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.CLAN_MEMBER);

        Page<ClanMember> page = clanMemberRepository.findAll(
                ClanSpecification.getClanMembersByClanId(clanId).and(
                        ClanSpecification.filterClanMembers(requestDto.getKeyword(), requestDto.getSearchBy())),
                pageable
        );

        List<GetClanMemberDetailResponseDto> items = page.getContent().stream()
                .map(GetClanMemberDetailResponseDto::new)
                .collect(Collectors.toList());

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.CLAN_MEMBER, page);

        PaginationResponseDto<GetClanMemberDetailResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public CommonResponseDto removeMember(Long clanId, Long memberId, Long playerId) {
        Clan clan = clanRepository.findById(clanId).
                orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_FOUND_ID, clanId));
        if (!Objects.equals(clan.getMaster().getId(), playerId)) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN);
        }

        ClanMember clanMember = clanMemberRepository.findByIdAndClanId(memberId, clanId).
                orElseThrow(() -> new NotFoundException(ErrorMessage.ClanMember.ERR_NOT_FOUND_ID, memberId));

        clanMemberRepository.delete(clanMember);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto promoteMember(Long clanId, Long memberId, Long playerId) {
        Clan clan = clanRepository.findById(clanId).
                orElseThrow(() -> new NotFoundException(ErrorMessage.Clan.ERR_NOT_FOUND_ID, clanId));
        if (!Objects.equals(clan.getMaster().getId(), playerId)) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN);
        }

        ClanMember clanMember = clanMemberRepository.findByIdAndClanId(memberId, clanId).
                orElseThrow(() -> new NotFoundException(ErrorMessage.ClanMember.ERR_NOT_FOUND_ID, memberId));

        clanMember.setRights(ClanMemberRightsConstants.CLAN_OFFICER);
        clanMemberRepository.save(clanMember);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

}
