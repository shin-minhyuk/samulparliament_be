package com.samulparliament_be.global.oauth;

import com.samulparliament_be.domain.users.dto.AuthProvider;
import com.samulparliament_be.global.oauth.dto.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoUserInfo getUserInfo(String code) {
        return new KakaoUserInfo(
                "test@test.test",
                "카카오유저",
                AuthProvider.KAKAO
        );
    }
}
