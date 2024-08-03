package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.config.properties.AdminInfo;
import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.RoleConstant;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.UserDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.ChangeUsernameRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateUserRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.entity.Player;
import com.tuyenngoc.army2forum.domain.entity.User;
import com.tuyenngoc.army2forum.domain.mapper.UserMapper;
import com.tuyenngoc.army2forum.exception.BadRequestException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.PlayerRepository;
import com.tuyenngoc.army2forum.repository.UserRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.PlayerCharactersService;
import com.tuyenngoc.army2forum.service.RoleService;
import com.tuyenngoc.army2forum.service.UserService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${user.change-username.price}")
    private int changeUsernamePrice;

    private final MessageSource messageSource;

    private final UserRepository userRepository;

    private final PlayerRepository playerRepository;

    private final UserMapper userMapper;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final PlayerCharactersService playerCharactersService;

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
                user.setEnabled(true);
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
    public UserDto getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME, username));
        return userMapper.toUserDto(user);
    }

    @Override
    public User updateUser(String userId, UpdateUserRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));

        user.setPhoneNumber(requestDto.getPhoneNumber());
        user.setFullName(requestDto.getFullName());

        return userRepository.save(user);
    }

    @Override
    public CommonResponseDto deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, userId);
        }

        userRepository.deleteById(userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));
    }

    @Override
    public PaginationResponseDto<User> getUsers(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.USER);

        Page<User> page = userRepository.findAll(pageable);
        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.USER, page);

        PaginationResponseDto<User> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public Boolean changeUsername(CustomUserDetails userDetails, ChangeUsernameRequestDto requestDto) {
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

        return Boolean.TRUE;
    }

}
