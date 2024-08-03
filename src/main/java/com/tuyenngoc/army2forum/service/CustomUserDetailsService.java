package com.tuyenngoc.army2forum.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService {

    UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException;

}
