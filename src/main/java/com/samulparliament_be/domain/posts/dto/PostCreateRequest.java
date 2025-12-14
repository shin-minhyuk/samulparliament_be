package com.samulparliament_be.domain.posts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 게시글을 생성하기 위한 요청 DTO
 *
 * @param title    게시글 제목
 * @param content  게시글 내용
 * @param imageUrl 게시글 대표 이미지 URL
 */
public record PostCreateRequest(
        // TODO: 인증 적용 후 authorId 제거 (AuthenticationPrincipal 사용)
        @NotNull
        Long authorId,

        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @NotBlank(message = "내용은 필수입니다.")
        String content,

        String imageUrl
) {
}
