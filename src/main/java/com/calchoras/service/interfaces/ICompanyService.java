package com.calchoras.service.interfaces;

import com.calchoras.model.Company;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for company operations.
 */
public interface ICompanyService {

    /**
     * Returns all companies.
     *
     * @return a list of companies
     */
    List<Company> findAll();

    /**
     * Returns a company by its ID.
     *
     * @param companyId the ID of the company
     * @return an Optional containing the company if found
     */
    Optional<Company> findById(int companyId);

    /**
     * Creates a new company.
     *
     * @param company the company to be saved
     * @return the created company
     */
    Company save(Company company);

    /**
     * Updates an existing company.
     *
     * @param company the company to be updated
     * @return the updated company
     */
    Company update(Company company);

    /**
     * Deletes a company by ID.
     *
     * @param companyId the ID of the company to delete
     * @return true if the company was deleted, false otherwise
     */
    boolean deleteById(int companyId);

    /**
     * Checks whether a company exists by ID.
     *
     * @param companyId the ID to check
     * @return true if the company exists
     */
    boolean existsById(int companyId);

    /**
     * Checks whether a company exists by name.
     *
     * @param name the name to check
     * @return true if a company with this name exists
     */
    boolean existsByName(String name);
}
