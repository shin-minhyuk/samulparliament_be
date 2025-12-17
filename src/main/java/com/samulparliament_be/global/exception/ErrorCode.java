package com.samulparliament_be.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 인증 관련
    INVALID_TOKEN("AUTH001", "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("AUTH002", "만료된 토큰입니다", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("AUTH003", "인증이 필요합니다", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("AUTH004", "접근 권한이 없습니다", HttpStatus.FORBIDDEN),
    
    // 유저 관련
    USER_NOT_FOUND("USER001", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL("USER002", "이미 존재하는 이메일입니다", HttpStatus.CONFLICT),
    
    // OAuth 관련
    OAUTH_PROVIDER_ERROR("OAUTH001", "OAuth 제공자 연결에 실패했습니다", HttpStatus.BAD_GATEWAY),
    UNSUPPORTED_OAUTH_PROVIDER("OAUTH002", "지원하지 않는 OAuth 제공자입니다", HttpStatus.BAD_REQUEST),
    OAUTH_TOKEN_ERROR("OAUTH003", "OAuth 토큰 발급에 실패했습니다", HttpStatus.BAD_GATEWAY),
    
    // 게시글 관련
    POST_NOT_FOUND("POST001", "게시글을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    POST_UPDATE_FORBIDDEN("POST002", "게시글 수정 권한이 없습니다", HttpStatus.FORBIDDEN),
    POST_DELETE_FORBIDDEN("POST003", "게시글 삭제 권한이 없습니다", HttpStatus.FORBIDDEN),
    
    // 공통
    INVALID_INPUT("COMMON001", "잘못된 입력입니다", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND("COMMON002", "리소스를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("COMMON003", "서버 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
