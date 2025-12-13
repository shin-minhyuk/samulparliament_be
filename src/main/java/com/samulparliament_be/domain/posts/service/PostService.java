package com.samulparliament_be.domain.posts.service;

import com.samulparliament_be.domain.posts.entity.Post;
import com.samulparliament_be.domain.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    // CREATE
    public Post create(Post post) {
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
    public Post update(Long id, String title, String content, String imageUrl) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.update(title, content, imageUrl);
        return post;
    }

    // DELETE
    public void delete(Long id) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        post.softDelete();
    }
}
