package com.calchoras.repository.interfaces;

import com.calchoras.model.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for employee persistence operations.
 */
public interface IEmployeeRepository {

    /**
     * Returns all employees.
     *
     * @return a list of employees
     */
    List<Employee> findAll();

    /**
     * Returns an employee by its ID.
     *
     * @param id the employee ID
     * @return an Optional containing the employee if found
     */
    Optional<Employee> findById(int id);

    /**
     * Returns all employees belonging to a specific company.
     *
     * @param companyId the company ID
     * @return a list of employees
     */
    List<Employee> findByCompanyId(int companyId);

    /**
     * Saves a new employee and returns the created instance with its ID assigned.
     *
     * @param employee the employee to save
     * @return the created employee
     */
    Employee save(Employee employee);

    /**
     * Updates an existing employee and returns the updated instance.
     *
     * @param employee the employee to update
     * @return the updated employee
     */
    Employee update(Employee employee);

    /**
     * Deletes an employee by its ID.
     *
     * @param id the employee ID
     * @return true if the employee was successfully deleted, false otherwise
     */
    boolean deleteById(int id);

    /**
     * Disables an employee by its ID
     *
     * @param id the employee ID
     * @return true if the employee was successfully disabled, false otherwise
     */
    boolean disableById(int id);

    /**
     * Enables an employee by its ID
     *
     * @param id the employee ID
     * @return true if the employee was successfully enabled, false otherwise
     */
    boolean enableById(int id);

    /**
     * Checks whether an employee exists by ID.
     *
     * @param id the employee ID
     * @return true if the employee exists
     */
    boolean existsById(int id);

    /**
     * Checks whether an employee exists by name.
     *
     * @param name the employee name
     * @return true if an employee with this name exists
     */
    boolean existsByName(String name);

    /**
     * Checks whether there are employees belonging to a specific company.
     *
     * @param companyId the company ID
     * @return true if at least one employee exists for the company
     */
    boolean existsByCompanyId(int companyId);
}
