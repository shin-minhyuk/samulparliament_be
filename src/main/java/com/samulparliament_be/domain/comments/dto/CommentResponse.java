package com.samulparliament_be.domain.comments.dto;

import java.time.LocalDateTime;

import com.samulparliament_be.domain.comments.entity.Comment;

public record CommentResponse(
  Long postId,
  String authorName,
  String authorEmail,
  String content,
  LocalDateTime createdAt,
  LocalDateTime updatedAt
) {
  public static CommentResponse from(Comment comment) {
    return new CommentResponse(
      comment.getPost().getId(),
      comment.getAuthorName(),
      comment.getAuthorEmail(),
      comment.getContent(),
      comment.getCreatedAt(),
      comment.getUpdatedAt()
    );
  }
}
