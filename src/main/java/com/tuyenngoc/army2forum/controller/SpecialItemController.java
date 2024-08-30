package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.RestApiV1;
import com.tuyenngoc.army2forum.base.VsResponseUtil;
import com.tuyenngoc.army2forum.constant.UrlConstant;
import com.tuyenngoc.army2forum.service.SpecialItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Special Item")
public class SpecialItemController {

    SpecialItemService specialItemService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "API Get Special Item")
    @GetMapping(UrlConstant.SpecialItem.GET_ALL)
    public ResponseEntity<?> getSpecialItem() {
        return VsResponseUtil.success(specialItemService.getSpecialItem());
    }

}
