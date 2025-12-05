package com.calchoras.service.interfaces;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;

/**
 * Service interface for daily calculations
 */
public interface IDailyCalculationService {

    /**
     * Calculates the daily hour balance of an employee.
     * @param timeEntry TimeEntry instance containing the time entries for a day
     * @param employee The employee for whom the calculation will be performed
     * @return A DailyCalculationResult instance containing the totals and details for the day
     */
    DailyCalculationResult calculate(TimeEntry timeEntry, Employee employee);
}