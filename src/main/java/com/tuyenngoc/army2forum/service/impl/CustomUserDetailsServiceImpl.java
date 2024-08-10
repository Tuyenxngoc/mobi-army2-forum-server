package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.domain.entity.User;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.UserRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {

    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_USERNAME, username));

        return CustomUserDetails.create(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));

        return CustomUserDetails.create(user);
    }

}