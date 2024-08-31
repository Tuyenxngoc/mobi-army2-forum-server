package com.tuyenngoc.army2forum.controller;

import com.tuyenngoc.army2forum.annotation.RestApiV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "GiftCode")
public class PlayerGiftCodeController {
}
