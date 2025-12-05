package com.calchoras.repository.interfaces;

import com.calchoras.model.Company;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for company persistence operations.
 */
public interface ICompanyRepository {

    /**
     * Returns all companies.
     *
     * @return a list of companies
     */
    List<Company> findAll();

    /**
     * Returns a company by its ID.
     *
     * @param id the company ID
     * @return an Optional containing the company if found
     */
    Optional<Company> findById(int id);

    /**
     * Saves a company and returns the created instance with its ID assigned.
     *
     * @param company the company to save
     * @return the created company
     */
    Company save(Company company);

    /**
     * Updates an existing company and returns the updated instance.
     *
     * @param company the company to update
     * @return the updated company
     */
    Company update(Company company);

    /**
     * Deletes a company by its ID.
     *
     * @param id the company ID
     * @return true if the company was successfully deleted, false otherwise
     */
    boolean deleteById(int id);

    /**
     * Checks whether a company exists by ID.
     *
     * @param id the company ID
     * @return true if the company exists
     */
    boolean existsById(int id);

    /**
     * Checks whether a company exists by name.
     *
     * @param name the company name
     * @return true if a company with this name exists
     */
    boolean existsByName(String name);
}
