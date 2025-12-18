package com.samulparliament_be.domain.comments.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.samulparliament_be.domain.comments.entity.Comment;

public record CommentResponse(
    Long id,
    Long postId,
    Long parentId,
    String authorName,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<CommentResponse> childrens
) {
    // 최상위 댓글용 (replies 포함)
    // 댓글의 deletedAt은 Repository에서 필터링
    // 대댓글은 CommentResponse에서 필터링
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getPost().getId(),
            comment.getParent() != null ? comment.getParent().getId() : null,
            comment.getAuthorName(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getUpdatedAt(),
            comment.getChildren().stream()
                .filter(c -> c.getDeletedAt() == null)
                .map(CommentResponse::fromReply)
                .toList()
        );
    }

    // 대댓글용 (replies 없음) 최대 대댓글 까지만
    public static CommentResponse fromReply(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getPost().getId(),
            comment.getParent() != null ? comment.getParent().getId() : null,
            comment.getAuthorName(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getUpdatedAt(),
            List.of()  // 대댓글은 하위 replies 없음
        );
    }
}

