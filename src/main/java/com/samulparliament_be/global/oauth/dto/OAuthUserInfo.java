package com.samulparliament_be.global.oauth.dto;

public record OAuthUserInfo(
        String name,
        String email,
        String profileImageUrl
) {
}
