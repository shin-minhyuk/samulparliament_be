package com.samulparliament_be.domain.posts.entity;

import com.samulparliament_be.domain.common.entity.BaseEntity;
import com.samulparliament_be.domain.posts.dto.PostCreateRequest;
import com.samulparliament_be.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(columnDefinition = "text")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(length = 50, nullable = false)
    private String authorName;

    @Column(nullable = false)
    private String authorEmail;

    public static Post create(User author, PostCreateRequest request) {
        return Post.builder()
                        .title(request.title())
                        .content(request.content())
                        .imageUrl(request.imageUrl())
                        .author(author)
                        .authorName(author.getName())
                        .authorEmail(author.getEmail())
                        .build();
        }

    public void update(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
