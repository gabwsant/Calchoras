package com.calchoras.mapper;

import com.calchoras.dto.EmployeeDTO;
import com.calchoras.model.Employee;

public class EmployeeMapper {

    // model to view
    public static EmployeeDTO toDTO(Employee entity) {
        if (entity == null) {
            return null;
        }
        return new EmployeeDTO(
                entity.getId(),
                entity.getCompanyId(),
                entity.getName(),
                entity.getShiftIn(),
                entity.getShiftOut(),
                entity.getLunchBreakMinutes(),
                entity.isActive()
        );
    }

    // view to model
    public static Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Employee(
                dto.id(),
                dto.companyId(),
                dto.name(),
                dto.shiftIn(),
                dto.shiftOut(),
                dto.lunchBreakMinutes(),
                dto.active()
        );
    }
}