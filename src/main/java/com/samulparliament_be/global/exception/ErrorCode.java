package com.samulparliament_be.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // ========== 공통 ==========
    /** [COMMON001] 400 Bad Request - 잘못된 입력입니다 */
    INVALID_INPUT("COMMON001", "잘못된 입력입니다", HttpStatus.BAD_REQUEST),
    /** [COMMON002] 404 Not Found - 리소스를 찾을 수 없습니다 */
    RESOURCE_NOT_FOUND("COMMON002", "리소스를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    /** [COMMON003] 500 Internal Server Error - 서버 오류가 발생했습니다 */
    INTERNAL_SERVER_ERROR("COMMON003", "서버 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR),

    // ========== 인증 관련 ==========
    /** [AUTH001] 401 Unauthorized - 유효하지 않은 토큰입니다 */
    INVALID_TOKEN("AUTH001", "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),
    /** [AUTH002] 401 Unauthorized - 만료된 토큰입니다 */
    EXPIRED_TOKEN("AUTH002", "만료된 토큰입니다", HttpStatus.UNAUTHORIZED),
    /** [AUTH003] 401 Unauthorized - 인증이 필요합니다 */
    UNAUTHORIZED("AUTH003", "인증이 필요합니다", HttpStatus.UNAUTHORIZED),
    /** [AUTH004] 403 Forbidden - 접근 권한이 없습니다 */
    ACCESS_DENIED("AUTH004", "접근 권한이 없습니다", HttpStatus.FORBIDDEN),
    
    // ========== 유저 관련 ==========
    /** [USER001] 404 Not Found - 사용자를 찾을 수 없습니다 */
    USER_NOT_FOUND("USER001", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    /** [USER002] 409 Conflict - 이미 존재하는 이메일입니다 */
    DUPLICATE_EMAIL("USER002", "이미 존재하는 이메일입니다", HttpStatus.CONFLICT),
    
    // ========== OAuth 관련 ==========
    /** [OAUTH001] 502 Bad Gateway - OAuth 제공자 연결에 실패했습니다 */
    OAUTH_PROVIDER_ERROR("OAUTH001", "OAuth 제공자 연결에 실패했습니다", HttpStatus.BAD_GATEWAY),
    /** [OAUTH002] 400 Bad Request - 지원하지 않는 OAuth 제공자입니다 */
    UNSUPPORTED_OAUTH_PROVIDER("OAUTH002", "지원하지 않는 OAuth 제공자입니다", HttpStatus.BAD_REQUEST),
    /** [OAUTH003] 502 Bad Gateway - OAuth 토큰 발급에 실패했습니다 */
    OAUTH_TOKEN_ERROR("OAUTH003", "OAuth 토큰 발급에 실패했습니다", HttpStatus.BAD_GATEWAY),
    
    // ========== 게시글 관련 ==========
    /** [POST001] 404 Not Found - 게시글을 찾을 수 없습니다 */
    POST_NOT_FOUND("POST001", "게시글이 존재하지 않습니다", HttpStatus.NOT_FOUND),
    /** [POST002] 403 Forbidden - 게시글 수정 권한이 없습니다 */
    POST_UPDATE_FORBIDDEN("POST002", "게시글 수정 권한이 없습니다", HttpStatus.FORBIDDEN),
    /** [POST003] 403 Forbidden - 게시글 삭제 권한이 없습니다 */
    POST_DELETE_FORBIDDEN("POST003", "게시글 삭제 권한이 없습니다", HttpStatus.FORBIDDEN),
    
    // ========== 댓글 관련 ==========
    /** [COMMENT001] 404 Not Found - 댓글을 찾을 수 없습니다 */
    COMMENT_NOT_FOUND("COMMENT001", "댓글이 존재하지 않습니다", HttpStatus.NOT_FOUND),
    /** [COMMENT002] 403 Forbidden - 댓글 수정 권한이 없습니다 */
    COMMENT_UPDATE_FORBIDDEN("COMMENT002", "댓글 수정 권한이 없습니다", HttpStatus.FORBIDDEN),
    /** [COMMENT003] 403 Forbidden - 댓글 삭제 권한이 없습니다 */
    COMMENT_DELETE_FORBIDDEN("COMMENT003", "댓글 삭제 권한이 없습니다", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
