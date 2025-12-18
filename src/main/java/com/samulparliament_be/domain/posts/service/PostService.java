package com.samulparliament_be.domain.posts.service;

import com.samulparliament_be.domain.posts.dto.PostCreateRequest;
import com.samulparliament_be.domain.posts.dto.PostUpdateRequest;
import com.samulparliament_be.domain.posts.entity.Post;
import com.samulparliament_be.domain.posts.repository.PostRepository;
import com.samulparliament_be.domain.users.entity.User;
import com.samulparliament_be.global.exception.BusinessException;
import com.samulparliament_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    // CREATE
    public Post create(User author, PostCreateRequest request) {
        Post post = Post.create(author, request);

        Post saved = postRepository.save(post);
        log.info("[POST] 게시글 생성 | postId={} authorId={}", saved.getId(), author.getId());
        return saved;
    }

    // READ
    @Transactional(readOnly = true)
    public Page<Post> getAll(Pageable pageable) {
        return postRepository.findByDeletedAtIsNull(pageable);
    }

    // READ
    @Transactional(readOnly = true)
    public Post get(Long id) {
        return postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }

    // UPDATE
    public Post update(Long id, User user, PostUpdateRequest request) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.POST_UPDATE_FORBIDDEN);
        }

        post.update(request.title(), request.content(), request.imageUrl());
        log.info("[POST] 게시글 수정 | postId={} userId={}", id, user.getId());
        return post;
    }

    // DELETE
    public void delete(Long id, User user) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.POST_DELETE_FORBIDDEN);
        }

        post.softDelete();
        log.info("[POST] 게시글 삭제 | postId={} userId={}", id, user.getId());
    }

    // ADMIN DELETE
    public void forceDelete(Long id) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.softDelete();
        log.info("[POST] 게시글 강제 삭제 (관리자) | postId={}", id);
    }
}
