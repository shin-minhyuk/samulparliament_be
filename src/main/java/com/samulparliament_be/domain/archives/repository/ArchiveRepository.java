package com.samulparliament_be.domain.archives.repository;

import com.samulparliament_be.domain.archives.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
}
