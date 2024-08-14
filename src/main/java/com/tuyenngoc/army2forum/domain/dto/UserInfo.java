package com.tuyenngoc.army2forum.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfo {

    private String userId;

    private String email;

    private boolean emailVerified;

}
