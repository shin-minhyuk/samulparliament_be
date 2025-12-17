package com.samulparliament_be.global.config;

import com.samulparliament_be.global.auth.provider.JwtAuthenticationFilter;
import com.samulparliament_be.global.auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("JWT 기반 인증만 사용합니다.");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 1. CSRF 비활성화 (JWT는 Stateless)
                .csrf(csrf -> csrf.disable())

                // 기본 로그인 폼 / Basic 인증 비활성화
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 2. 세션 사용 안 함 (JWT 기반)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. URL 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 1. Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()


                        // 2. 인증 API 로그인 만 공개
                        .requestMatchers(HttpMethod.POST, "/api/auth/login/**").permitAll()

                        // 3. 민감한 GET 경로는 먼저 차단
                        .requestMatchers(HttpMethod.GET, "/api/admin/**").authenticated()

                        // 4. 모든 GET은 공개
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()

                        // ❗ 그 외는 로그인 필수
                        .anyRequest().authenticated()
                )

                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
                // → 요청마다 AccessToken 검사 후 SecurityContext에 사용자 저장
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}