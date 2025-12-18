package com.samulparliament_be.domain.comments.service;

import org.springframework.stereotype.Service;

import com.samulparliament_be.domain.comments.dto.CommentRequest;
import com.samulparliament_be.domain.comments.dto.CommentResponse;
import com.samulparliament_be.domain.comments.entity.Comment;
import com.samulparliament_be.domain.comments.repository.CommentRepository;
import com.samulparliament_be.domain.posts.entity.Post;
import com.samulparliament_be.domain.posts.repository.PostRepository;
import com.samulparliament_be.domain.users.entity.User;
import com.samulparliament_be.global.exception.BusinessException;
import com.samulparliament_be.global.exception.ErrorCode;

import jakarta.transaction.TransactionScoped;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@TransactionScoped
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;

	public CommentResponse create(User user, Long postId, CommentRequest request) {
		Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
				.orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

		Comment comment = Comment.create(user, post, request.content());
		commentRepository.save(comment);

		return CommentResponse.from(comment);
	}

	public CommentResponse update(User user, Long commentId, CommentRequest request) {
		Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
				.orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

		comment.update(request.content());
		commentRepository.save(comment);

		return CommentResponse.from(comment);
	}

	public void delete(User user, Long commentId) {
		Comment comment = commentRepository.findByIdAndDeletedAtIsNull(commentId)
				.orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

		if (!comment.getAuthor().getId().equals(user.getId())) {
			throw new BusinessException(ErrorCode.COMMENT_DELETE_FORBIDDEN);
		}

		comment.softDelete();
	}
}
