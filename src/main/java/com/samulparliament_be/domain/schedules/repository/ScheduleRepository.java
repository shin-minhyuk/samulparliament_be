package com.samulparliament_be.domain.schedules.repository;

import com.samulparliament_be.domain.schedules.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByDeletedAtIsNull(Pageable pageable);
    Optional<Schedule> findByIdAndDeletedAtIsNull(Long id);
}
