package com.tuyenngoc.army2forum.service;

import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.request.CreateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.giftcode.GetGiftCodeDetailResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.giftcode.GetGiftCodeResponseDto;
import com.tuyenngoc.army2forum.domain.dto.response.giftcode.GetPlayerGiftCodeResponseDto;

public interface GiftCodeService {

    GetGiftCodeDetailResponseDto getGiftCodeDetailById(Long id);

    PaginationResponseDto<GetGiftCodeResponseDto> getGiftCodes(PaginationFullRequestDto requestDto);

    CommonResponseDto createGiftCode(CreateGiftCodeRequestDto requestDto);

    CommonResponseDto updateGiftCode(Long id, UpdateGiftCodeRequestDto requestDto);

    CommonResponseDto deleteGiftCode(Long id);

    PaginationResponseDto<GetPlayerGiftCodeResponseDto> getPlayersByGiftCodeId(Long id, PaginationFullRequestDto requestDto);

}
