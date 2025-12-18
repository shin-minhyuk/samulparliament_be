package com.samulparliament_be.domain.comments.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samulparliament_be.domain.comments.dto.CommentRequest;
import com.samulparliament_be.domain.comments.dto.CommentResponse;
import com.samulparliament_be.domain.comments.entity.Comment;
import com.samulparliament_be.domain.comments.repository.CommentRepository;
import com.samulparliament_be.domain.posts.entity.Post;
import com.samulparliament_be.domain.posts.repository.PostRepository;
import com.samulparliament_be.domain.users.entity.User;
import com.samulparliament_be.global.exception.BusinessException;
import com.samulparliament_be.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // 최상위 댓글 생성
    public CommentResponse create(User user, Long postId, CommentRequest request) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Comment comment = Comment.create(user, post, request.content());
        commentRepository.save(comment);

        return CommentResponse.from(comment);
    }

    // 대댓글 생성 (2단계 중첩 제한)
    public CommentResponse createReply(User user, Long commentId, CommentRequest request) {
        Comment parent = commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        // 2단계 중첩 제한: 최상위 댓글에만 대댓글 가능
        if (!parent.canHaveReply()) {
            throw new BusinessException(ErrorCode.REPLY_DEPTH_EXCEEDED);
        }

        Comment reply = Comment.createReply(user, parent, request.content());
        commentRepository.save(reply);

        return CommentResponse.fromReply(reply);
    }

    // 댓글 수정
    public CommentResponse update(User user, Long commentId, CommentRequest request) {
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.COMMENT_UPDATE_FORBIDDEN);
        }

        comment.update(request.content());

        return CommentResponse.fromReply(comment);
    }

    // 댓글 삭제
    public void delete(User user, Long commentId) {
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.COMMENT_DELETE_FORBIDDEN);
        }

        comment.softDelete();
    }
}
