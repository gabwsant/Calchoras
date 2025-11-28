package com.calchoras.service.interfaces;

import com.calchoras.model.Company;
import com.calchoras.model.Employee;

import java.util.List;
import java.util.Optional;

public interface ICompanyService {

    List<Company> getAllCompanies();
    Optional<Company> getCompanyById(int companyId);
    Company addCompany(Company company);
    void updateCompany(Company company);
    void deleteCompany(int companyId);
    boolean exists(int companyId);
}
