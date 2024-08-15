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
public class ForgetPasswordRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Pattern(regexp = CommonConstant.REGEXP_USERNAME, message = ErrorMessage.INVALID_FORMAT_USERNAME)
    private String username;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Email(message = ErrorMessage.INVALID_FORMAT_EMAIL)
    @Size(max = 255, message = ErrorMessage.INVALID_TEXT_LENGTH)
    @Size(min = 5, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String email;

}
