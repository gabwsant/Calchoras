package com.calchoras.service;

import com.calchoras.dto.CompanyDTO;
import com.calchoras.mapper.CompanyMapper;
import com.calchoras.model.Company;
import com.calchoras.repository.interfaces.ICompanyRepository;
import com.calchoras.service.interfaces.ICompanyService;

import java.util.List;
import java.util.Optional;

public class CompanyService implements ICompanyService {

    private final ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<CompanyDTO> findAll() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(CompanyMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<CompanyDTO> findById(int companyId) {
        return companyRepository.findById(companyId)
                .map(CompanyMapper::toDTO);
    }

    @Override
    public CompanyDTO save(CompanyDTO companyDTO) {
        if (companyRepository.existsByName(companyDTO.name())) {
            throw new IllegalArgumentException("Empresa já existe.");
        }

        Company company = CompanyMapper.toEntity(companyDTO);
        Company savedCompany = companyRepository.save(company);

        return CompanyMapper.toDTO(savedCompany);
    }

    @Override
    public CompanyDTO update(CompanyDTO companyDTO) {
        if (!companyRepository.existsById(companyDTO.id())) {
            throw new IllegalArgumentException("Empresa não encontrada para atualização.");
        }

        Company company = CompanyMapper.toEntity(companyDTO);
        Company savedCompany = companyRepository.update(company);

        return CompanyMapper.toDTO(savedCompany);
    }

    @Override
    public boolean deleteById(int companyId) {
        if (!companyRepository.existsById(companyId)) {
            return false;
        }
        return companyRepository.deleteById(companyId);
    }

    @Override
    public boolean existsById(int companyId) {
        return companyRepository.existsById(companyId);
    }

    @Override
    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }

}
