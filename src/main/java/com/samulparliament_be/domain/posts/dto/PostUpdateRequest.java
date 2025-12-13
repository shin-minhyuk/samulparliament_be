package com.samulparliament_be.domain.posts.dto;

/**
 * 게시글을 업데이트 하기 위한 요청 DTO
 *
 * @param title    게시글 제목
 * @param content  게시글 내용
 * @param imageUrl 게시글 대표 이미지 URL
 */
public record PostUpdateRequest(
        String title,

        String content,

        String imageUrl
) {
}
