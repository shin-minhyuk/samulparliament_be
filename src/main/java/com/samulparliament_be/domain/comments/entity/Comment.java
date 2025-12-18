package com.samulparliament_be.domain.comments.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // 자기 참조: 부모 댓글 (null이면 최상위 댓글, 값이 있으면 대댓글)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 자식 댓글들 (대댓글 목록)
    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<Comment> children = new ArrayList<>();

    @Column(name = "author_name", length = 50, nullable = false)
    private String authorName;

    @Column(name = "author_email", nullable = false)
    private String authorEmail;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    // 최상위 댓글 생성
    public static Comment create(User author, Post post, String content) {
        return Comment.builder()
                .author(author)
                .post(post)
                .parent(null)
                .authorName(author.getName())
                .authorEmail(author.getEmail())
                .content(content)
                .build();
    }

    // 대댓글 생성
    public static Comment createReply(User author, Comment parent, String content) {
        return Comment.builder()
                .author(author)
                .post(parent.getPost())
                .parent(parent)
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

    // 대댓글 가능 여부 체크 (2단계 중첩 제한)
    public boolean canHaveReply() {
        return this.parent == null;
    }
}

