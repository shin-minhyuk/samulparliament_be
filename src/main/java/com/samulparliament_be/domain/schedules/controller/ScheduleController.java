package com.samulparliament_be.domain.schedules.controller;

import org.hibernate.query.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.samulparliament_be.domain.schedules.service.ScheduleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleService scheduleService;

    // @GetMapping
    // public Page<ScheduleResponse> getAll() {
    //     return scheduleService.getAll();
    // }
}
