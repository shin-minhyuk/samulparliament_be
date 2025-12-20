package com.samulparliament_be.domain.schedules.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.samulparliament_be.domain.schedules.entity.Schedule;
import com.samulparliament_be.domain.schedules.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional(readOnly = true)
    public Page<Schedule> getAll(Pageable pageable) {
        return scheduleRepository.findByDeletedAtIsNull(pageable);
    }

}
