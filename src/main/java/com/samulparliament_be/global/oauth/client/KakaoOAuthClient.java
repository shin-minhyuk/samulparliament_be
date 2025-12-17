package com.samulparliament_be.global.oauth.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samulparliament_be.domain.users.dto.AuthProvider;
import com.samulparliament_be.global.oauth.dto.KakaoUserInfoResponse;
import com.samulparliament_be.global.oauth.dto.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClient {

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth.kakao.token-uri}")
    private String tokenUri;

    @Value("${oauth.kakao.user-info-uri}")
    private String userInfoUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public AuthProvider provider() {
        return AuthProvider.KAKAO;
    }

    @Override
    public OAuthUserInfo getUserInfo(String code) {
        String accessToken = getAccessToken(code);
        return getUserProfile(accessToken);
    }


    /**
     * 1. code → access_token
     */
    private String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                tokenUri,
                request,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    /**
     * 2. access_token → 사용자 정보
     */
    private OAuthUserInfo getUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserInfoResponse> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                request,
                KakaoUserInfoResponse.class
        );
        
        KakaoUserInfoResponse body = response.getBody();

        String email = body.kakao_account().email();
        String name = body.kakao_account().profile().nickname();
        String profileImageUrl = body.kakao_account().profile().profile_image_url();

        return new OAuthUserInfo(name, email, profileImageUrl);
    }
}
