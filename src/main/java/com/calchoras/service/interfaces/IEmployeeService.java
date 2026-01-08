package com.calchoras.service.interfaces;

import com.calchoras.dto.EmployeeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for employee operations.
 */
public interface IEmployeeService {

    /**
     * Returns all employees.
     */
    List<EmployeeDTO> findAll();

    /**
     * Returns an employee by its ID.
     *
     * @param employeeId the ID of the employee
     * @return an Optional containing the employee if found
     */
    Optional<EmployeeDTO> findById(int employeeId);

    /**
     * Returns all employees belonging to a company.
     *
     * @param companyId the company ID
     * @return a list of employees
     */
    List<EmployeeDTO> findByCompanyId(int companyId);

    /**
     * Returns ACTIVES employees belonging to a company.
     *
     * @param companyId the company ID
     * @return a list of active employees
     */
    List<EmployeeDTO> findActivesByCompanyId(int companyId);

    /**
     * Creates a new employee.
     *
     * @param employeeDTO the employee to be saved
     * @return the created employee
     */
    EmployeeDTO save(EmployeeDTO employeeDTO);

    /**
     * Updates an existing employee.
     *
     * @param employeeDTO the employee to be updated
     * @return the updated employee
     */
    EmployeeDTO update(EmployeeDTO employeeDTO);

    /**
     * Deletes an employee by ID.
     *
     * @param employeeId the ID of the employee to delete
     * @return true if the employee was deleted, false otherwise
     */
    boolean deleteById(int employeeId);

    /**
     * Disable an employee by its ID
     *
     * @param employeeId the ID of the employee to disable
     * @return true if the employee was disabled, false otherwise
     */
    boolean disableById(int employeeId);

    /**
     * Enables an employee by its ID
     *
     * @param employeeId the ID of the employee to enable
     * @return true if the employee was enabled, false otherwise
     */
    boolean enableById(int employeeId);

    /**
     * Checks whether an employee exists by ID.
     *
     * @param employeeId the ID to check
     * @return true if the employee exists
     */
    boolean existsById(int employeeId);

    /**
     * Checks whether an employee exists by name.
     *
     * @param name the name to check
     * @return true if an employee with this name exists
     */
    boolean existsByName(String name);
}
