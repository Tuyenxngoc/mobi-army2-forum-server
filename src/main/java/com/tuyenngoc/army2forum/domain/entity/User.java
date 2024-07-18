package com.tuyenngoc.army2forum.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tuyenngoc.army2forum.domain.entity.common.DateAuditing;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends DateAuditing {

    @Id
    @UuidGenerator
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "full-name")
    private String fullName;

    @Column(name = "phone-number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "FK_USER_ROLE_ID"), referencedColumnName = "role_id", nullable = false)
    @JsonIgnore
    private Role role;

}
