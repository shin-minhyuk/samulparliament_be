package com.samulparliament_be.global.posts.entity;

import com.samulparliament_be.global.common.entity.BaseEntity;
import com.samulparliament_be.global.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    private String authorName;
    private String authorEmail;
}
