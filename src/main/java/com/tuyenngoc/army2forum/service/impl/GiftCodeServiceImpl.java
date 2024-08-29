package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.GiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.repository.GiftCodeRepository;
import com.tuyenngoc.army2forum.service.GiftCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftCodeServiceImpl implements GiftCodeService {

    private final GiftCodeRepository giftCodeRepository;

    @Override
    public Object getGiftCodeById(Long id) {
        return null;
    }

    @Override
    public PaginationResponseDto<Object> getGiftCodes(PaginationFullRequestDto requestDto) {
        return null;
    }

    @Override
    public CommonResponseDto createGiftCode(GiftCodeRequestDto requestDto) {
        return null;
    }

    @Override
    public CommonResponseDto updateGiftCode(Long id, GiftCodeRequestDto requestDto) {
        return null;
    }

    @Override
    public CommonResponseDto deleteGiftCode(Long id) {
        return null;
    }

}
