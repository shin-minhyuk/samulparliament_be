package com.samulparliament_be.domain.schedules.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import com.samulparliament_be.domain.schedules.entity.Schedule;

public record ScheduleResponse(
    Long id,
    String title,
    String description,
    LocalDate scheduledDate,
    LocalTime startTime,
    LocalTime endTime,
    boolean isImportant
) {
    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
            schedule.getId(),
            schedule.getTitle(),
            schedule.getDescription(),
            schedule.getScheduledDate(),
            schedule.getStartTime(),
            schedule.getEndTime(),
            schedule.isImportant()
        );
    }
}
