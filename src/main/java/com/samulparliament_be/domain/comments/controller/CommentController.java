package com.samulparliament_be.domain.comments.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samulparliament_be.domain.comments.dto.CommentRequest;
import com.samulparliament_be.domain.comments.dto.CommentResponse;
import com.samulparliament_be.domain.comments.service.CommentService;
import com.samulparliament_be.global.auth.annotation.USER;
import com.samulparliament_be.global.auth.details.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 최상위 댓글 생성
    @USER
    @PostMapping
    public CommentResponse create(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long postId,
            @RequestBody CommentRequest request) {
        return commentService.create(userDetails.getUser(), postId, request);
    }

    // 대댓글 생성
    @USER
    @PostMapping("/{commentId}/replies")
    public CommentResponse createReply(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long commentId,
            @RequestBody CommentRequest request) {
        return commentService.createReply(userDetails.getUser(), commentId, request);
    }

    // 댓글 수정
    @USER
    @PutMapping("/{commentId}")
    public CommentResponse update(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long commentId,
            @RequestBody CommentRequest request) {
        return commentService.update(userDetails.getUser(), commentId, request);
    }

    // 댓글 삭제
    @USER
    @DeleteMapping("/{commentId}")
    public void delete(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long commentId) {
        commentService.delete(userDetails.getUser(), commentId);
    }
}


