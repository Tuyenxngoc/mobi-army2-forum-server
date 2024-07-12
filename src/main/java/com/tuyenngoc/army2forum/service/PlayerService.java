package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.entity.Player;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface PlayerService {

    Player updatePlayerRoles(Long playerId, Long roleId, Collection<? extends GrantedAuthority> authorities);

}
