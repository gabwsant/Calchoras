package com.calchoras.service.interfaces;

import com.calchoras.model.Employee;
import com.calchoras.model.PeriodCalculationResult;
import com.calchoras.model.TimeEntry;

import java.util.List;

/**
 * Service interface for generating reports.
 */
public interface IReportService {

    /**
     * Calculates the consolidated hour balance for an employee in a given period.
     *
     * @param employee The employee for whom the calculation will be performed.
     * @param entries  The list of time entries for the period.
     * @return A PeriodCalculationResult instance containing daily totals and details.
     */
    PeriodCalculationResult calculatePeriodBalance(Employee employee, List<TimeEntry> entries);
}
