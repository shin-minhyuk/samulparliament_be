package com.samulparliament_be.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 비즈니스 예외 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException e, 
            HttpServletRequest request) {
        
        ErrorCode errorCode = e.getErrorCode();
        
        log.warn("[{}] {} | URI: {} | Method: {}", 
                errorCode.getCode(),
                e.getMessage(),
                request.getRequestURI(),
                request.getMethod());
        
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, e.getMessage()));
    }

    // Validation 예외 처리 (@Valid 실패 시)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request) {
        
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("유효성 검사 실패");
        
        log.warn("[VALIDATION] {} | URI: {} | Method: {}", 
                errorMessage,
                request.getRequestURI(),
                request.getMethod());
        
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT, errorMessage));
    }

    // IllegalArgumentException 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException e,
            HttpServletRequest request) {
        
        log.warn("[INVALID_INPUT] {} | URI: {} | Method: {}",
                e.getMessage(),
                request.getRequestURI(),
                request.getMethod());
        
        return ResponseEntity
                .status(ErrorCode.INVALID_INPUT.getStatus())
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT, e.getMessage()));
    }

    // 예상치 못한 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception e, 
            HttpServletRequest request) {
        
        log.error("[UNEXPECTED] URI: {} | Method: {} | Error: {}",
                request.getRequestURI(),
                request.getMethod(),
                e.getMessage(),
                e);
        
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
