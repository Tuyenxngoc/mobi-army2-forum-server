package com.tuyenngoc.army2forum.domain.dto.request.auth;

import com.tuyenngoc.army2forum.constant.CommonConstant;
import com.tuyenngoc.army2forum.constant.ErrorMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Pattern(regexp = CommonConstant.REGEXP_FULL_NAME, message = ErrorMessage.INVALID_FORMAT_NAME)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    @Size(min = 2, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String fullName;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Pattern(regexp = CommonConstant.REGEXP_PHONE_NUMBER, message = ErrorMessage.INVALID_FORMAT_PHONE)
    @Size(max = 20, message = ErrorMessage.INVALID_TEXT_LENGTH)
    @Size(min = 10, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String phoneNumber;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Pattern(regexp = CommonConstant.REGEXP_USERNAME, message = ErrorMessage.INVALID_FORMAT_USERNAME)
    private String username;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Pattern(regexp = CommonConstant.REGEXP_PASSWORD, message = ErrorMessage.INVALID_FORMAT_PASSWORD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String password;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String repeatPassword;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Email(message = ErrorMessage.INVALID_FORMAT_EMAIL)
    @Size(max = 255, message = ErrorMessage.INVALID_TEXT_LENGTH)
    @Size(min = 5, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String email;

}
