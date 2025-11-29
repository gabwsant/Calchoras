package com.calchoras.service;

import com.calchoras.model.Company;
import com.calchoras.repository.interfaces.ICompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes para o CompanyService")
class CompanyServiceTest {

    private ICompanyRepository companyRepository;
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        companyRepository = mock(ICompanyRepository.class);
        companyService = new CompanyService(companyRepository);
    }

    @Test
    @DisplayName("Deve retornar todas as empresas")
    void findAll_ShouldReturnAllCompanies() {
        Company c1 = new Company(1, "Locarvel", 0);
        Company c2 = new Company(2, "Gontijo", 5);

        when(companyRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Company> result = companyService.findAll();

        assertEquals(2, result.size());
        verify(companyRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar empresa por ID")
    void findById_ShouldReturnCompany() {
        Company company = new Company(1, "Locarvel", 0);
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));

        Optional<Company> result = companyService.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Locarvel", result.get().getName());
        verify(companyRepository).findById(1);
    }

    @Test
    @DisplayName("Deve salvar empresa e retornar o objeto com ID")
    void save_ShouldAssignIdAndReturnCompany() {
        Company company = new Company(0, "Locarvel", 0);
        when(companyRepository.existsByName("Locarvel")).thenReturn(false);
        when(companyRepository.findAll()).thenReturn(List.of());
        when(companyRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Company saved = companyService.save(company);

        assertNotNull(saved);
        assertEquals(1, saved.getId());
        assertEquals("Locarvel", saved.getName());
        verify(companyRepository).save(saved);
    }

    @Test
    @DisplayName("Deve atualizar empresa existente")
    void update_ShouldReturnUpdatedCompany() {
        Company company = new Company(1, "Locarvel", 0);
        when(companyRepository.existsById(1)).thenReturn(true);
        when(companyRepository.update(company)).thenReturn(company);

        Company updated = companyService.update(company);

        assertEquals(company, updated);
        verify(companyRepository).update(company);
    }

    @Test
    @DisplayName("Deve remover empresa por ID")
    void deleteById_ShouldReturnTrueIfDeleted() {
        when(companyRepository.existsById(1)).thenReturn(true);
        when(companyRepository.deleteById(1)).thenReturn(true);

        boolean deleted = companyService.deleteById(1);

        assertTrue(deleted);
        verify(companyRepository).deleteById(1);
    }

    @Test
    @DisplayName("Deve retornar falso se empresa não existir ao deletar")
    void deleteById_ShouldReturnFalseIfNotExists() {
        when(companyRepository.existsById(1)).thenReturn(false);

        boolean deleted = companyService.deleteById(1);

        assertFalse(deleted);
        verify(companyRepository, never()).deleteById(anyInt());
    }
}
