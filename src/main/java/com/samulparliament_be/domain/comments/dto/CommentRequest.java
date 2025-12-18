package com.samulparliament_be.domain.comments.dto;

public record CommentRequest(
  String content
) {
  public static CommentRequest from(String content) {
    return new CommentRequest(content);
  }
}
