package com.samulparliament_be.domain.archives.entity;

import com.samulparliament_be.global.common.entity.BaseEntity;
import com.samulparliament_be.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "archives")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Archive extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(columnDefinition = "text", nullable = false)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(length = 25, nullable = false)
    private FileType type;

    @Column(columnDefinition = "text")
    private String fileUrl;

    @Column(columnDefinition = "text")
    private String thumbnailUrl;

    @Column(length = 20)
    private String category;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "archive_tags",
        joinColumns = @JoinColumn(name = "archive_id")
    )
    @Column(name = "tag")
    private Set<String> tags;

    public enum FileType {
        IMAGE, VIDEO, TEXT, PDF
    }
}
