package com.samulparliament_be.global.oauth.dto;

public record KakaoUserInfoResponse(
    KakaoAccount kakao_account
) {
    public record KakaoAccount(
        String email,
        Profile profile
    ) {
        public record Profile(
            String nickname,
            String profile_image_url
        ) {}
    }
}