package com.samulparliament_be.global.faqs.entity;

import com.samulparliament_be.global.common.entity.BaseEntity;
import com.samulparliament_be.global.users.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "faqs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Faq extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(columnDefinition = "text", nullable = false)
    private String question;

    @Column(columnDefinition = "text", nullable = false)
    private String answer;

    @Column(length = 50, columnDefinition = "text")
    private String category;
}
