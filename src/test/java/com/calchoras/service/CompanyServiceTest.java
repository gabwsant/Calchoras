package com.calchoras.service;

import com.calchoras.dto.CompanyDTO;
import com.calchoras.mapper.CompanyMapper;
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
        Company c1 = new Company(1, "Locarvel");
        Company c2 = new Company(2, "Gontijo");

        when(companyRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CompanyDTO> result = companyService.findAll();

        assertEquals(2, result.size());
        verify(companyRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar empresa por ID")
    void findById_ShouldReturnCompany() {
        Company company = new Company(1, "Locarvel");
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));

        Optional<CompanyDTO> result = companyService.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Locarvel", result.get().name());
        verify(companyRepository).findById(1);
    }

    @Test
    @DisplayName("Deve salvar empresa e retornar o objeto com ID")
    void save_ShouldAssignIdAndReturnCompany() {
        CompanyDTO company = new CompanyDTO(1, "Locarvel");
        when(companyRepository.existsByName("Locarvel")).thenReturn(false);
        when(companyRepository.findAll()).thenReturn(List.of());
        when(companyRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CompanyDTO saved = companyService.save(company);

        assertNotNull(saved);
        assertEquals(1, saved.id());
        assertEquals("Locarvel", saved.name());
        verify(companyRepository).save(CompanyMapper.toEntity(saved));
    }

    @Test
    @DisplayName("Deve atualizar empresa existente")
    void update_ShouldReturnUpdatedCompany() {
        Company company = new Company(1, "Locarvel");
        when(companyRepository.existsById(1)).thenReturn(true);
        when(companyRepository.update(company)).thenReturn(company);

        CompanyDTO updated = companyService.update(CompanyMapper.toDTO(company));

        assertEquals(CompanyMapper.toDTO(company), updated);
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
