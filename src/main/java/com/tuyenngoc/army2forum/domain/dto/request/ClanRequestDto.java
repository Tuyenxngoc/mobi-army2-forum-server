package com.tuyenngoc.army2forum.domain.dto.request;


import com.tuyenngoc.army2forum.constant.CommonConstant;
import com.tuyenngoc.army2forum.constant.ErrorMessage;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClanRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 50, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String name;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 255, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String description;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 20, message = ErrorMessage.INVALID_TEXT_LENGTH)
    @Pattern(regexp = CommonConstant.REGEXP_PHONE_NUMBER, message = ErrorMessage.INVALID_FORMAT_PHONE)
    private String phoneNumber;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    @Email(message = ErrorMessage.INVALID_FORMAT_EMAIL)
    private String email;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private Short icon;

}
