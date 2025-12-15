package com.samulparliament_be.global.auth.provider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터
 *
 * 모든 HTTP 요청마다 실행되며,
 * Authorization 헤더에 JWT가 있으면
 * 해당 토큰을 검증하고 Spring Security에 인증 정보를 등록한다.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // JWT 생성 / 검증 / 인증 객체 생성을 담당하는 Provider
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 생성자
     *
     * JwtAuthenticationFilter는 JWT 로직을 직접 처리하지 않고,
     * JwtTokenProvider에게 위임한다.
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 모든 HTTP 요청마다 단 한 번 실행되는 메서드
     *
     * 요청이 Controller에 도달하기 전에 실행된다.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,   // 현재 HTTP 요청
            HttpServletResponse response, // 현재 HTTP 응답
            FilterChain filterChain       // 다음 필터 또는 컨트롤러로 넘기기 위한 체인
    ) throws ServletException, IOException {

        // 1. HTTP 요청 헤더에서 JWT 토큰을 추출
        String token = resolveToken(request);

        // 2. 토큰이 존재하고, 유효하다면
        if (token != null && jwtTokenProvider.validateToken(token)) {

            // 3. 토큰을 기반으로 Authentication 객체 생성
            Authentication authentication =
                    jwtTokenProvider.getAuthentication(token);

            // 4. Spring Security에게 "이 요청은 인증된 사용자"라고 알려줌
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        // 5️⃣ 다음 필터 또는 컨트롤러로 요청을 넘김
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청에서 Authorization 헤더를 읽어 JWT 토큰만 추출하는 메서드
     *
     * 예시:
     * Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
     */
    private String resolveToken(HttpServletRequest request) {

        // Authorization 헤더 값 조회
        String bearer = request.getHeader("Authorization");

        // Bearer 토큰 형식인지 확인
        if (bearer != null && bearer.startsWith("Bearer ")) {

            // "Bearer " 이후의 실제 토큰 문자열만 추출
            return bearer.substring(7);
        }

        // 토큰이 없거나 형식이 맞지 않으면 null 반환
        return null;
    }
}