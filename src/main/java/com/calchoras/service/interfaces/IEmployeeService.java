package com.calchoras.service.interfaces;

import com.calchoras.model.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for employee operations.
 */
public interface IEmployeeService {

    /**
     * Returns all employees.
     */
    List<Employee> findAll();

    /**
     * Returns an employee by its ID.
     *
     * @param employeeId the ID of the employee
     * @return an Optional containing the employee if found
     */
    Optional<Employee> findById(int employeeId);

    /**
     * Returns all employees belonging to a company.
     *
     * @param companyId the company ID
     * @return a list of employees
     */
    List<Employee> findByCompanyId(int companyId);

    /**
     * Creates a new employee.
     *
     * @param employee the employee to be saved
     * @return the created employee
     */
    Employee save(Employee employee);

    /**
     * Updates an existing employee.
     *
     * @param employee the employee to be updated
     * @return the updated employee
     */
    Employee update(Employee employee);

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
    boolean disableEmployee(int employeeId);

    /**
     * Enables an employee by its ID
     *
     * @param employeeId the ID of the employee to enable
     * @return true if the employee was enabled, false otherwise
     */
    boolean enableEmployee(int employeeId);

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
