package com.samulparliament_be.domain.schedules.repository;

import com.samulparliament_be.domain.schedules.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByDeletedAtIsNull();
    Optional<Schedule> findByIdAndDeletedAtIsNull();
}
