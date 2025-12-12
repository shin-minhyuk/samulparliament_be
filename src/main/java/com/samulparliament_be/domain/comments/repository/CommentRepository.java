package com.samulparliament_be.domain.comments.repository;

import com.samulparliament_be.domain.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByDeletedAtIsNull();
    Optional<Comment> findByIdAndDeletedAtIsNull(Long id);
}
