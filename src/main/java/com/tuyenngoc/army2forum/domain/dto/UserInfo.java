package com.tuyenngoc.army2forum.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfo {

    private String email;

    private String name;

    private String phoneNumber;

    private boolean emailVerified;

}
