package com.samulparliament_be.domain.comments.entity;

import java.time.LocalDateTime;
import com.samulparliament_be.domain.common.entity.BaseEntity;
import com.samulparliament_be.domain.posts.entity.Post;
import com.samulparliament_be.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "author_name", length = 50, nullable = false)
    private String authorName;

    @Column(name = "author_email", nullable = false)
    private String authorEmail;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    public static Comment create(User author, Post post, String content) {
        return Comment.builder()
                            .author(author)
                            .post(post)
                            .authorName(author.getName())
                            .authorEmail(author.getEmail())
                            .content(content)
                            .build();
    }

    public Comment update(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
