package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.CreateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;

public interface GiftCodeService {

    Object getGiftCodeById(Long id);

    PaginationResponseDto<Object> getGiftCodes(PaginationFullRequestDto requestDto);

    CommonResponseDto createGiftCode(CreateGiftCodeRequestDto requestDto);

    CommonResponseDto updateGiftCode(Long id, UpdateGiftCodeRequestDto requestDto);

    CommonResponseDto deleteGiftCode(Long id);

}
