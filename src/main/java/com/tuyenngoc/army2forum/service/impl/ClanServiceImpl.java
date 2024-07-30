package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.ClanRequestDto;
import com.tuyenngoc.army2forum.repository.ClanRepository;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.ClanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClanServiceImpl implements ClanService {

    private final ClanRepository clanRepository;

    @Override
    public boolean createClan(ClanRequestDto requestDto, CustomUserDetails userDetails) {
        return false;
    }

    @Override
    public boolean updateClan(Long id, ClanRequestDto requestDto, CustomUserDetails userDetails) {
        return false;
    }

    @Override
    public boolean deleteClan(Long id, CustomUserDetails userDetails) {
        return false;
    }

    @Override
    public boolean getClanById(Long id) {
        return false;
    }

    @Override
    public boolean getClans(PaginationFullRequestDto requestDto) {
        return false;
    }

}
