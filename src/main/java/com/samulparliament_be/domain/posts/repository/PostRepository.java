package com.samulparliament_be.domain.posts.repository;

import com.samulparliament_be.domain.posts.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByDeletedAtIsNull(Pageable pageable);
    Optional<Post> findByIdAndDeletedAtIsNull(Long id);
}
