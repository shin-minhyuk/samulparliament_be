package com.samulparliament_be.domain.schedules.service;

import org.hibernate.query.Page;
import org.springframework.stereotype.Service;
import com.samulparliament_be.domain.schedules.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;


    // public Page<ScheduleResponse> getAll(int page, int size, String sort) {

    // }
    
}
