package com.samulparliament_be.domain.users.entity;

import com.samulparliament_be.global.common.entity.BaseEntity;
import com.samulparliament_be.domain.users.dto.AuthProvider;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    public enum Role {
        USER, ADMIN
    }

    public static User create(String email, String name, String profileImageUrl,
            AuthProvider provider) {
        return User.builder().email(email).name(name).profileImageUrl(profileImageUrl)
                .provider(provider).role(Role.USER).build();
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}

