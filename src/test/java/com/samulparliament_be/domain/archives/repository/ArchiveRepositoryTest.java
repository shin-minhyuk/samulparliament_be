package com.samulparliament_be.domain.archives.repository;

import com.samulparliament_be.domain.archives.entity.Archive;
import com.samulparliament_be.domain.users.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ArchiveRepositoryTest {

    @Autowired
    private ArchiveRepository archiveRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Archive 엔티티가 정상적으로 저장됩니다.")
    void save_archive_success() {
        // givne - User 먼저 저장
        User user = User.builder()
                .email("test@test.com")
                .name("민혁")
                .role(User.Role.USER)
                .build();

        em.persist(user);

        Archive archive = Archive.builder()
                .author(user)
                .title("테스트 아카이브")
                .description("테스트 소개")
                .date(LocalDate.parse("2025-10-22"))
                .fileUrl("https://file.url")
                .thumbnailUrl("https://thumb.url")
                .type(Archive.FileType.IMAGE)
                .tags(Set.of("태그1", "태그2"))
                .build();

        // when
        Archive saved = archiveRepository.save(archive);
        em.flush();
        em.clear();

        // then
        Archive found = archiveRepository.findById(saved.getId()).orElseThrow();

        assertThat(found.getTitle()).isEqualTo("테스트 아카이브");
        assertThat(found.getDescription()).isEqualTo("테스트 소개");
        assertThat(found.getType()).isEqualTo(Archive.FileType.IMAGE);
        assertThat(found.getTags()).containsExactlyInAnyOrder("태그1", "태그2");
    }

}
