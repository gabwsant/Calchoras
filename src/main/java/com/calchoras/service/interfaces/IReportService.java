package com.calchoras.service.interfaces;

import com.calchoras.dto.EmployeeDTO;
import com.calchoras.dto.TimeEntryDTO;
import com.calchoras.model.PeriodCalculationResult;

import java.util.List;

/**
 * Service interface for generating reports.
 */
public interface IReportService {

    /**
     * Calculates the consolidated hour balance for an employee in a given period.
     *
     * @param employeeDTO The employee for whom the calculation will be performed.
     * @param entries  The list of time entries for the period.
     * @return A PeriodCalculationResult instance containing daily totals and details.
     */
    PeriodCalculationResult calculatePeriodBalance(EmployeeDTO employeeDTO, List<TimeEntryDTO> entries);
}
