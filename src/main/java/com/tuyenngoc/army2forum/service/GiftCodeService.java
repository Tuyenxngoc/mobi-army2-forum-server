package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.GiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;

public interface GiftCodeService {

    Object getGiftCodeById(Long id);

    PaginationResponseDto<Object> getGiftCodes(PaginationFullRequestDto requestDto);

    CommonResponseDto createGiftCode(GiftCodeRequestDto requestDto);

    CommonResponseDto updateGiftCode(Long id, GiftCodeRequestDto requestDto);

    CommonResponseDto deleteGiftCode(Long id);

}
