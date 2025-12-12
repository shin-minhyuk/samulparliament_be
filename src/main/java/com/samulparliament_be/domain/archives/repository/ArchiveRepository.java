package com.samulparliament_be.domain.archives.repository;

import com.samulparliament_be.domain.archives.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    List<Archive> findByDeletedAtIsNull();
    Optional<Archive> findByIdAndDeletedAtIsNull(Long id);
}
