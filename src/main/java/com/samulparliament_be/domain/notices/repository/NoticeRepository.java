package com.samulparliament_be.domain.notices.repository;

import com.samulparliament_be.domain.notices.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByDeletedAtIsNull();
    Optional<Notice> findByIdAndDeletedAtIsNull(Long id);
}
