package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.CreateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.UpdateGiftCodeRequestDto;
import com.tuyenngoc.army2forum.service.GiftCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "GiftCode")
public class GiftCodeController {

    GiftCodeService giftCodeService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Get GiftCode By Id")
    @GetMapping(UrlConstant.GiftCode.GET_BY_ID)
    public ResponseEntity<?> getGiftCodeById(@PathVariable Long id) {
        return VsResponseUtil.success(giftCodeService.getGiftCodeDetailById(id));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Get GiftCodes")
    @GetMapping(UrlConstant.GiftCode.GET_ALL)
    public ResponseEntity<?> getGiftCodesForAdmin(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(giftCodeService.getGiftCodes(requestDto));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "API Create GiftCode")
    @PostMapping(UrlConstant.GiftCode.CREATE)
    public ResponseEntity<?> createGiftCode(@Valid @RequestBody CreateGiftCodeRequestDto requestDto) {
        return VsResponseUtil.success(giftCodeService.createGiftCode(requestDto));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Update GiftCode")
    @PutMapping(UrlConstant.GiftCode.UPDATE)
    public ResponseEntity<?> updateGiftCode(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGiftCodeRequestDto requestDto
    ) {
        return VsResponseUtil.success(giftCodeService.updateGiftCode(id, requestDto));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "API Delete GiftCode")
    @DeleteMapping(UrlConstant.GiftCode.DELETE)
    public ResponseEntity<?> deleteGiftCode(@PathVariable Long id) {
        return VsResponseUtil.success(giftCodeService.deleteGiftCode(id));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @Operation(summary = "API Get Players by GiftCode Id")
    @GetMapping(UrlConstant.GiftCode.GET_PLAYERS_BY_ID)
    public ResponseEntity<?> getPlayersByGiftCodeId(
            @PathVariable Long id,
            @ParameterObject PaginationFullRequestDto requestDto
    ) {
        return VsResponseUtil.success(giftCodeService.getPlayersByGiftCodeId(id, requestDto));
    }
}
