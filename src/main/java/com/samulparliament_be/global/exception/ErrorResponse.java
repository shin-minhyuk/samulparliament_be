package com.samulparliament_be.global.exception;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    
    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode, String customMessage) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(customMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(String message) {
        return ErrorResponse.builder()
                .code("UNKNOWN")
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
