package com.samulparliament_be.domain.schedules.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.samulparliament_be.domain.schedules.dto.ScheduleResponse;
import com.samulparliament_be.domain.schedules.service.ScheduleService;
import com.samulparliament_be.global.common.dto.OrderType;
import com.samulparliament_be.global.common.dto.PageResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleService scheduleService;

    @GetMapping
    public PageResponse<ScheduleResponse> getAll(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "DESC") OrderType order) {
        Pageable pageable =
                PageRequest.of(page, size,
                Sort.by(order == OrderType.ASC ? Sort.Direction.ASC
                                : Sort.Direction.DESC,
                                "createdAt"));

        Page<ScheduleResponse> schedulePage = scheduleService.getAll(pageable).map(ScheduleResponse::from);

        return PageResponse.from(schedulePage);
    }
}
