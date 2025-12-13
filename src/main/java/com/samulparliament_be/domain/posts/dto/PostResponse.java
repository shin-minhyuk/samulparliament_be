package com.samulparliament_be.domain.posts.dto;

import com.samulparliament_be.domain.posts.entity.Post;

import java.time.LocalDateTime;

public record PostResponse(
    Long id,
    String title,
    String content,
    String imageUrl,
    String authorName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getAuthorName(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
