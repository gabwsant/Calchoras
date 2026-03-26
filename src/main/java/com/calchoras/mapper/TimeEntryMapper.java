package com.calchoras.mapper;

import com.calchoras.dto.TimeEntryDTO;
import com.calchoras.model.TimeEntry;

import java.util.List;
import java.util.stream.Collectors;

public class TimeEntryMapper {

    // model to view
    public static TimeEntryDTO toDTO(TimeEntry entity) {
        if (entity == null) {
            return null;
        }
        return new TimeEntryDTO(
                entity.getId(),
                entity.getEmployeeId(),
                entity.getEntryDate(),
                entity.getClockIn(),
                entity.getLunchIn(),
                entity.getLunchOut(),
                entity.getClockOut(),
                entity.isDayOff()
        );
    }

    public static List<TimeEntryDTO> toDTO(List<TimeEntry> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(TimeEntryMapper::toDTO)
                .toList();
    }

    // view to model
    public static TimeEntry toEntity(TimeEntryDTO dto) {
        if (dto == null) {
            return null;
        }

        return new TimeEntry(
                dto.id(),
                dto.employeeId(),
                dto.entryDate(),
                dto.clockIn(),
                dto.lunchIn(),
                dto.lunchOut(),
                dto.clockOut(),
                dto.dayOff()
        );
    }


    public static List<TimeEntry> toEntity(List<TimeEntryDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }

        return dtos.stream()
                .map(TimeEntryMapper::toEntity)
                .toList();
    }
}