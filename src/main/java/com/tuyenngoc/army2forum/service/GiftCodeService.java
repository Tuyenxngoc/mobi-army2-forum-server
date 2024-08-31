package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.CreateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.GetGiftCodeDetailResponseDto;
import com.tuyenngoc.army2forum.domain.entity.GiftCode;

public interface GiftCodeService {

    GetGiftCodeDetailResponseDto getGiftCodeDetailById(Long id);

    PaginationResponseDto<GiftCode> getGiftCodes(PaginationFullRequestDto requestDto);

    CommonResponseDto createGiftCode(CreateGiftCodeRequestDto requestDto);

    CommonResponseDto updateGiftCode(Long id, UpdateGiftCodeRequestDto requestDto);

    CommonResponseDto deleteGiftCode(Long id);

}
