package com.tuyenngoc.army2forum.domain.dto.request;

import com.tuyenngoc.army2forum.constant.ErrorMessage;
import com.tuyenngoc.army2forum.domain.json.EquipChest;
import com.tuyenngoc.army2forum.domain.json.SpecialItemChest;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGiftCodeRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(min = 3, max = 20, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String code;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    @Min(value = 0, message = ErrorMessage.INVALID_MINIMUM_ZERO)
    @Max(value = 32000, message = ErrorMessage.INVALID_MAXIMUM_SHORT)
    private Short usageLimit;

    private LocalDateTime expirationDate;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    @Min(value = 0, message = ErrorMessage.INVALID_MINIMUM_ZERO)
    @Max(value = 2_000_000_000, message = ErrorMessage.INVALID_MAXIMUM_INT)
    private Integer xu;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    @Min(value = 0, message = ErrorMessage.INVALID_MINIMUM_ZERO)
    @Max(value = 2_000_000_000, message = ErrorMessage.INVALID_MAXIMUM_INT)
    private Integer luong;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    @Min(value = 0, message = ErrorMessage.INVALID_MINIMUM_ZERO)
    @Max(value = 2_000_000_000, message = ErrorMessage.INVALID_MAXIMUM_INT)
    private Integer exp;

    @Size(max = 10, message = ErrorMessage.INVALID_ARRAY_LENGTH)
    private List<EquipChest> equips = new ArrayList<>();

    @Size(max = 10, message = ErrorMessage.INVALID_ARRAY_LENGTH)
    private List<SpecialItemChest> items = new ArrayList<>();

}
