package com.samulparliament_be.domain.users.dto;

import com.samulparliament_be.domain.users.entity.User;

public record UserResponse(
        Long id,
        String email,
        String name,
        String role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }
}
