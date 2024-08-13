package com.tuyenngoc.army2forum.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "admin")
public class AdminInfo {

    private String username;

    private String password;

    private String email;

    private String name;

    private String phoneNumber;

}
