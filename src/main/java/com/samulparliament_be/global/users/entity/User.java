package com.samulparliament_be.global.users.entity;

import com.samulparliament_be.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER, ADMIN
    }
}

