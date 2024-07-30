package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.ClanRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;

public interface ClanService {

    boolean createClan(ClanRequestDto requestDto, CustomUserDetails userDetails);

    boolean updateClan(Long id, ClanRequestDto requestDto, CustomUserDetails userDetails);

    boolean deleteClan(Long id, CustomUserDetails userDetails);

    boolean getClanById(Long id);

    boolean getClans(PaginationFullRequestDto requestDto);

}
