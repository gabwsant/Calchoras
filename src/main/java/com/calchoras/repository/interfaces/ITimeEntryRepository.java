package com.calchoras.repository.interfaces;

import com.calchoras.model.TimeEntry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for time entry persistence operations.
 */
public interface ITimeEntryRepository {

    /**
     * Returns all time entries.
     *
     * @return a list of time entries
     */
    List<TimeEntry> findAll();

    /**
     * Returns a time entry by its ID.
     *
     * @param id the time entry ID
     * @return an Optional containing the time entry if found
     */
    Optional<TimeEntry> findById(int id);

    /**
     * Returns all time entries for a given employee.
     *
     * @param employeeId the employee ID
     * @return a list of time entries
     */
    List<TimeEntry> findByEmployeeId(int employeeId);

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
     * @param entry the time entry to save
     * @return the created time entry
     */
    TimeEntry save(TimeEntry entry);

    /**
     * Updates an existing time entry and returns the updated instance.
     *
     * @param entry the time entry to update
     * @return the updated time entry
     */
    TimeEntry update(TimeEntry entry);

    /**
     * Deletes a time entry by its ID.
     *
     * @param id the time entry ID
     * @return true if the entry was successfully deleted, false otherwise
     */
    boolean deleteById(int id);

    /**
     * Deletes the time entry of an employee for a specific date.
     *
     * @param employeeId the employee ID
     * @param date the date of the time entry
     * @return true if the entry was successfully deleted, false otherwise
     */
    boolean deleteByEmployeeIdAndDate(int employeeId, LocalDate date);

    /**
     * Checks whether a time entry exists for an employee on a specific date.
     *
     * @param employeeId the employee ID
     * @param date the date of the time entry
     * @return true if a time entry exists, false otherwise
     */
    boolean existsByEmployeeIdAndDate(int employeeId, LocalDate date);
}
