package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.CurrentUser;
import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.domain.dto.pagination.PaginationFullRequestDto;
import com.tuyenngoc.army2forum.domain.dto.request.ClanRequestDto;
import com.tuyenngoc.army2forum.security.CustomUserDetails;
import com.tuyenngoc.army2forum.service.ClanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@Tag(name = "Clan")
public class ClanController {

    private final ClanService clanService;

    @Operation(summary = "API Create Clan")
    @PostMapping(UrlConstant.Clan.CREATE)
    public ResponseEntity<?> createClan(
            @Valid @RequestBody ClanRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(clanService.createClan(requestDto, userDetails));
    }

    @Operation(summary = "API Update Clan")
    @PutMapping(UrlConstant.Clan.UPDATE)
    public ResponseEntity<?> updateClan(
            @PathVariable Long id,
            @Valid @RequestBody ClanRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(clanService.updateClan(id, requestDto, userDetails));
    }

    @Operation(summary = "API Delete Clan")
    @DeleteMapping(UrlConstant.Clan.DELETE)
    public ResponseEntity<?> deleteClan(
            @PathVariable Long id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(clanService.deleteClan(id, userDetails));
    }

    @Operation(summary = "API Get Clans")
    @GetMapping(UrlConstant.Clan.GET_ALL)
    public ResponseEntity<?> getClans(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(clanService.getClans(requestDto));
    }

    @Operation(summary = "API Get Clan By Id ")
    @GetMapping(UrlConstant.Clan.GET_BY_ID)
    public ResponseEntity<?> getClanById(@PathVariable Long id) {
        return VsResponseUtil.success(clanService.getClanById(id));
    }

}
