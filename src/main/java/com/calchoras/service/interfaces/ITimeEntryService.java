package com.calchoras.service.interfaces;

import com.calchoras.dto.TimeEntryDTO;
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
    List<TimeEntryDTO> findByEmployeeId(int employeeId);

    /**
     * Returns a time entry by its ID.
     *
     * @param id the time entry ID
     * @return an Optional containing the time entry if found
     */
    Optional<TimeEntryDTO> findById(int id);

    /**
     * Returns the time entry of an employee for a specific date.
     *
     * @param employeeId the employee ID
     * @param date the date of the time entry
     * @return an Optional containing the time entry if found
     */
    Optional<TimeEntryDTO> findByEmployeeIdAndDate(int employeeId, LocalDate date);

    /**
     * Saves a new time entry and returns the created object with its ID assigned.
     *
     * @param timeEntryDTO the time entry to save
     * @return the created time entry
     */
    TimeEntryDTO save(TimeEntryDTO timeEntryDTO);

    /**
     * Updates an existing time entry.
     *
     * @param timeEntryDTO the updated time entry
     * @return the updated time entry
     */
    TimeEntryDTO update(TimeEntryDTO timeEntryDTO);

    /**
     * Decides if insert a new time entry or update if it already exists
     * @param timeEntryDTO time entry to insert or update
     * @return the inserted or updated time entry
     */
    TimeEntryDTO saveOrUpdate(TimeEntryDTO timeEntryDTO);

    /**
     * Deletes a time entry of an employee for a specific date.
     *
     * @param employeeId the employee ID
     * @param date the date of the time entry
     * @return true if the entry was deleted successfully, false otherwise
     */
    boolean deleteByEmployeeIdAndDate(int employeeId, LocalDate date);
}
