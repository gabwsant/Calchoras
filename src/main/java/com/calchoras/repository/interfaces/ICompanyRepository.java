package com.calchoras.repository.interfaces;

import com.calchoras.model.Company;

import java.util.List;
import java.util.Optional;

public interface ICompanyRepository {

    // Retorna todas as empresas
    List<Company> findAll();

    // Retorna uma empresa por ID
    Optional<Company> findById(int id);

    // Adiciona uma empresa e retorna o objeto criado com ID definido
    Company save(Company company);

    // Atualiza uma empresa e retorna o objeto atualizado
    Company update(Company company);

    // Remove uma empresa pelo ID e retorna um boolean indicando sucesso
    boolean deleteById(int id);

    // Verifica existência por ID
    boolean existsById(int id);

    // Verifica existência por nome
    boolean existsByName(String name);
}
