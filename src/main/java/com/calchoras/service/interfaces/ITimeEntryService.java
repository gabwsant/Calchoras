package com.calchoras.service.interfaces;

import com.calchoras.model.TimeEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing employee time entries.
 */
public interface ITimeEntryService {

    /**
     * Returns all time entries for a given employee.
     *
     * @param employeeId the ID of the employee
     * @return a list of the employee's time entries
     */
    List<TimeEntry> findByEmployeeId(int employeeId);

    /**
     * Returns a time entry by its ID.
     *
     * @param id the time entry ID
     * @return an Optional containing the time entry if found
     */
    Optional<TimeEntry> findById(int id);

    /**
     * Returns the time entry of an employee for a specific date.
     *
     * @param employeeId the employee ID
     * @param date the date of the time entry
     * @return an Optional containing the time entry if found
     */
    Optional<TimeEntry> findByEmployeeIdAndDate(int employeeId, LocalDate date);

    /**
     * Saves a new time entry and returns the created object with its ID assigned.
     *
     * @param timeEntry the time entry to save
     * @return the created time entry
     */
    TimeEntry save(TimeEntry timeEntry);

    /**
     * Updates an existing time entry.
     *
     * @param timeEntry the updated time entry
     * @return the updated time entry
     */
    TimeEntry update(TimeEntry timeEntry);

    /**
     * Deletes a time entry of an employee for a specific date.
     *
     * @param employeeId the employee ID
     * @param date the date of the time entry
     * @return true if the entry was deleted successfully, false otherwise
     */
    boolean deleteByEmployeeIdAndDate(int employeeId, LocalDate date);
}
