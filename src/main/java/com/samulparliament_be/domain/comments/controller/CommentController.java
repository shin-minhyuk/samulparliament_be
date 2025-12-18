package com.samulparliament_be.domain.comments.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samulparliament_be.domain.comments.dto.CommentRequest;
import com.samulparliament_be.domain.comments.dto.CommentResponse;
import com.samulparliament_be.domain.comments.service.CommentService;
import com.samulparliament_be.global.auth.annotation.USER;
import com.samulparliament_be.global.auth.details.UserDetailsImpl;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @USER
  @PostMapping
  public CommentResponse create(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long postId, @RequestBody CommentRequest request) {
    return commentService.create(userDetails.getUser(), postId, request);
  }

  @USER
  @PutMapping("/{commentId}")
  public CommentResponse update(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long commentId, @RequestBody CommentRequest request) {
    return commentService.update(userDetails.getUser(), commentId, request);
  }

  @USER
  @DeleteMapping("/{commentId}")
  public void delete(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long commentId) {
    commentService.delete(userDetails.getUser(), commentId);
  }
}

