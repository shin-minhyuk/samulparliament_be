package com.samulparliament_be.domain.posts.service;

import com.samulparliament_be.domain.posts.dto.PostCreateRequest;
import com.samulparliament_be.domain.posts.dto.PostUpdateRequest;
import com.samulparliament_be.domain.posts.entity.Post;
import com.samulparliament_be.domain.posts.repository.PostRepository;
import com.samulparliament_be.domain.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    // CREATE
    public Post create(User author, PostCreateRequest request) {

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .imageUrl(request.imageUrl())
                .author(author)
                .authorName(author.getName())
                .authorEmail(author.getEmail())
                .build();

        return postRepository.save(post);
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
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    // UPDATE
    public Post update(Long id, User user, PostUpdateRequest request) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // INFO: 작성자 검증
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("게시글 수정 권한이 없습니다.");
        }

        post.update(request.title(), request.content(), request.imageUrl());

        // save() 안 해도 됨 (dirty checking)
        return post;
    }

    // DELETE
    public void delete(Long id, User user) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // INFO: 작성자 검증
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("게시글 삭제 권한이 없습니다.");
        }

        post.softDelete();
    }

    // ADMIN DELETE
    public void forceDelete(Long id) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.softDelete();
    }
}
