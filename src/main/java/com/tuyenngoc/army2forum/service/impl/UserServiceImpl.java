package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.RoleConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.ClanDto;
import com.tuyenngoc.army2forum.domain.dto.ClanMemberDto;
import com.tuyenngoc.army2forum.domain.dto.PlayerDto;
import com.tuyenngoc.army2forum.domain.dto.request.ChangeUsernameRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.LockUserRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.entity.ClanMember;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.Role;
import com.tuyenngoc.army2forum.domain.entity.User;
import com.tuyenngoc.army2forum.exception.BadRequestException;
import com.tuyenngoc.army2forum.exception.ForbiddenException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.UserRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.JwtTokenService;
import com.tuyenngoc.army2forum.service.PlayerCharactersService;
import com.tuyenngoc.army2forum.service.RoleService;
import com.tuyenngoc.army2forum.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {

    @Value("${user.change-username.price:200}")
    int changeUsernamePrice;

    final MessageSource messageSource;

    final UserRepository userRepository;

    final PlayerRepository playerRepository;

    final RoleService roleService;

    final PasswordEncoder passwordEncoder;

    final PlayerCharactersService playerCharactersService;

    final JwtTokenService jwtTokenService;

    @Override
    public void initAdmin(AdminInfo adminInfo) {
        if (userRepository.count() == 0) {
            try {
                User user = new User();
                user.setUsername(adminInfo.getUsername());
                user.setEmail(adminInfo.getEmail());
                user.setPhoneNumber(adminInfo.getPhoneNumber());
                user.setFullName(adminInfo.getName());
                user.setPassword(passwordEncoder.encode(adminInfo.getPassword()));
                user.setRole(roleService.getRole(RoleConstant.ROLE_ADMIN.name()));
                user.setIsEnabled(true);
                user.setIsLocked(false);
                userRepository.save(user);

                Player player = new Player();
                player.setUser(user);
                playerRepository.save(player);

                playerCharactersService.initiatePlayerCharacterDefaults(player);

                log.info("Create admin user successfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));
    }

    @Override
    public PlayerDto getCurrentUser(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Player.ERR_NOT_FOUND_ID, playerId));

        PlayerDto responseDto = new PlayerDto(player);
        ClanMember clanMember = player.getClanMember();
        if (clanMember != null) {
            ClanDto clanDto = new ClanDto(clanMember.getClan());

            ClanMemberDto clanMemberDto = new ClanMemberDto();
            clanMemberDto.setRights(clanMember.getRights());
            clanMemberDto.setClan(clanDto);

            responseDto.setClanMember(clanMemberDto);
        }

        return responseDto;
    }

    @Override
    public CommonResponseDto updateUserRoles(Long playerId, Byte roleId, CustomUserDetails userDetails) {
        Role newRole = roleService.getRole(roleId);

        if (newRole.getName().equals(RoleConstant.ROLE_SUPER_ADMIN.name())) {
            throw new BadRequestException(ErrorMessage.User.ERR_NOT_ALLOWED_SUPER_ADMIN);
        }

        User user = userRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, playerId));

        user.setRole(newRole);
        userRepository.save(user);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto lockUserAccount(Long playerId, LockUserRequestDto requestDto, CustomUserDetails userDetails) {
        User user = userRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, playerId));

        if (user.getRole().getName().equals(RoleConstant.ROLE_ADMIN.name()) ||
                user.getRole().getName().equals(RoleConstant.ROLE_SUPER_ADMIN.name())
        ) {
            throw new ForbiddenException(ErrorMessage.ERR_FORBIDDEN_UPDATE_DELETE);
        }

        user.setIsLocked(true);
        user.setLockUntil(requestDto.getLockTime());
        user.setLockReason(requestDto.getLockReason());

        jwtTokenService.deleteTokens(user.getId());

        userRepository.save(user);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto changeUsername(CustomUserDetails userDetails, ChangeUsernameRequestDto requestDto) {
        User user = getUserById(userDetails.getUserId());

        if (userRepository.existsByUsername(requestDto.getNewUsername())) {
            throw new BadRequestException(ErrorMessage.Auth.ERR_DUPLICATE_USERNAME);
        }

        Player player = user.getPlayer();
        if (player.getLuong() < changeUsernamePrice) {
            throw new BadRequestException(ErrorMessage.Player.ERR_NOT_ENOUGH_MONEY);
        }
        player.setLuong(player.getLuong() - changeUsernamePrice);
        playerRepository.save(player);

        user.setUsername(requestDto.getNewUsername());
        userRepository.save(user);

        String message = messageSource.getMessage(SuccessMessage.User.CHANGE_USERNAME, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, true);
    }

}
