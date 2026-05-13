package com.calchoras.service.interfaces;

import com.calchoras.model.Employee;
import com.calchoras.model.PeriodCalculationResult;
import com.calchoras.model.TimeEntry;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for generating reports.
 */
public interface IReportService {

    /**
     * Calculates the consolidated hour balance for an employee in a given period.
     *
     * @param employee             The employee for whom the calculation will be performed.
     * @param entries              The list of time entries for the period.
     * @param initialDate          The initial date to consider when calculating.
     * @param finalDate            The final date to consider when calculating.
     * @param dailyAllowedLateness The minutes the employee is allowed to be late and don't make negative hours.
     * @param dailyAllowedPerPunch The minutes the employee is allowed to be late per punch.
     * @return A PeriodCalculationResult instance containing daily totals and details.
     */
    PeriodCalculationResult calculatePeriodBalance(
            Employee employee,
            List<TimeEntry> entries,
            LocalDate initialDate,
            LocalDate finalDate,
            int dailyAllowedLateness,
            int dailyAllowedPerPunch
    );
}
