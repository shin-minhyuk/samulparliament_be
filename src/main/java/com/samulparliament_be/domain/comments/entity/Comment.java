package com.samulparliament_be.domain.comments.entity;

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

    @Column(length = 50, nullable = false)
    private String author_name;

    @Column(nullable = false)
    private String author_email;

    @Column(columnDefinition = "text", nullable = false)
    private String content;
}
