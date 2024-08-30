package com.tuyenngoc.army2forum.service.impl;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.constant.SortByDataConstant;
import com.tuyenngoc.army2forum.constant.SuccessMessage;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationResponseDto;
import com.tuyenngoc.army2forum.domain.dto.pagination.PagingMeta;
import com.tuyenngoc.army2forum.domain.dto.request.CreateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.response.CommonResponseDto;
import com.tuyenngoc.army2forum.domain.entity.GiftCode;
import com.tuyenngoc.army2forum.domain.specification.GiftCodeSpecification;
import com.tuyenngoc.army2forum.exception.ConflictException;
import com.tuyenngoc.army2forum.exception.NotFoundException;
import com.tuyenngoc.army2forum.repository.GiftCodeRepository;
import com.tuyenngoc.army2forum.service.GiftCodeService;
import com.tuyenngoc.army2forum.util.PaginationUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GiftCodeServiceImpl implements GiftCodeService {

    GiftCodeRepository giftCodeRepository;

    MessageSource messageSource;

    @Override
    public GiftCode getGiftCodeById(Long id) {
        return giftCodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.GiftCode.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public PaginationResponseDto<GiftCode> getGiftCodes(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.GIFT_CODES);

        Page<GiftCode> page = giftCodeRepository.findAll(
                GiftCodeSpecification.filterGiftCodes(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable
        );

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.GIFT_CODES, page);

        PaginationResponseDto<GiftCode> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(page.getContent());
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    public CommonResponseDto createGiftCode(CreateGiftCodeRequestDto requestDto) {
        boolean existsByCode = giftCodeRepository.existsByCode(requestDto.getCode());
        if (existsByCode) {
            throw new ConflictException(ErrorMessage.GiftCode.ERR_DUPLICATE_CODE, requestDto.getCode());
        }

        GiftCode giftCode = new GiftCode();
        giftCode.setCode(requestDto.getCode());
        giftCode.setUsageLimit(requestDto.getUsageLimit());
        giftCode.setExpirationDate(requestDto.getExpirationDate());
        giftCode.setXu(requestDto.getXu());
        giftCode.setLuong(requestDto.getLuong());
        giftCode.setExp(requestDto.getExp());
        giftCode.setEquips(requestDto.getEquips());
        giftCode.setItems(requestDto.getItems());

        giftCodeRepository.save(giftCode);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto updateGiftCode(Long id, UpdateGiftCodeRequestDto requestDto) {

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto deleteGiftCode(Long id) {
        GiftCode giftCode = getGiftCodeById(id);

        giftCodeRepository.delete(giftCode);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

}
