package com.calchoras.service;

import com.calchoras.model.Company;
import com.calchoras.repository.CompanyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para o CompanyService")
class CompanyServiceTest {

    private final String TEST_FILE_PATH = "empresas_teste.json";
    private CompanyService companyService;
    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() {
        deleteTestFile();
        companyRepository = new CompanyRepository(TEST_FILE_PATH);
        companyService = new CompanyService(companyRepository);
    }

    @AfterEach
    void tearDown() {
        deleteTestFile();
    }

    private void deleteTestFile() {
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        } catch (IOException e) {
            System.err.println("Falha ao deletar arquivo de teste: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Deve adicionar empresa, atribuir um ID e aumentar a lista")
    void addCompany_ShouldAssignIdAndIncreaseListSize(){
        assertEquals(0, companyService.getAllCompanies().size(), "A lista deveria começar vazia.");
        Company company = new Company(
                1,
                "Locarvel",
                0
        );

        Company addedCompany = companyService.addCompany(company);

        assertNotNull(addedCompany, "A empresa retornada não deveria ser nulo.");
        assertEquals(1, addedCompany.getId(), "O ID da primeira empresa deveria ser 1.");
        assertEquals("Locarvel", addedCompany.getName(), "O nome da empresa deveria ser Locarvel.");
        assertEquals(0, addedCompany.getNumOfEmployees(), "A empresa deveria ter 0 funcionários.");
    }

    @Test
    @DisplayName("Deve persistir a empresa no arquivo após adicionar.")
    void addCompany_ShouldPersistDataToFile() {
        Company company = new Company(
                1,
                "Locarvel",
                0
        );

        companyService.addCompany(company);

        CompanyService newInstanceService =
                new CompanyService(new CompanyRepository(TEST_FILE_PATH));
        List<Company> loadedCompanies = newInstanceService.getAllCompanies();

        assertEquals(1, loadedCompanies.size(), "Deveria ter carregado 1 empresa do arquivo.");
        assertEquals("Locarvel", loadedCompanies.get(0).getName(), "O nome da empresa deveria ser Locarvel.");
    }

    @Test
    @DisplayName("Deve atualizar os dados de uma empresa existente")
    void updateCompany_ShouldModifyExistingCompany() {
        // Arrange
        Company originalCompany = new Company(
                1,
                "Locarvel",
                0
        );
        Company addedCompany = companyService.addCompany(originalCompany);

        addedCompany.setName("Gontijo");

        // Act
        companyService.updateCompany(addedCompany);
        Optional<Company> updatedCompanyOpt = companyService.getCompanyById(addedCompany.getId());

        // Assert
        assertTrue(updatedCompanyOpt.isPresent(), "A empresa atualizada deveria ser encontrado.");
        assertEquals("Gontijo", updatedCompanyOpt.get().getName());
    }

    @Test
    @DisplayName("Deve remover uma empresa da lista e do arquivo")
    void deleteCompany_ShouldRemoveCompany() {
        // Arrange
        Company company1 = new Company(
                1,
                "Locarvel",
                0
        );

        Company company2 = new Company(
                2,
                "Gontijo",
                0
        );

        companyService.addCompany(company1);
        companyService.addCompany(company2);

        assertEquals(2, companyService.getAllCompanies().size());

        // Act
        companyService.deleteCompany(company1.getId());

        // Assert (na instância atual)
        assertEquals(1, companyService.getAllCompanies().size(), "A lista deveria ter apenas 1 empresa.");
        assertFalse(companyService.getCompanyById(company1.getId()).isPresent(), "A empresa 1 não deveria mais existir.");
        assertTrue(companyService.getCompanyById(company2.getId()).isPresent(), "A empresa 2 deveria continuar existindo.");

        // Assert (verificando a persistência)
        CompanyService newInstanceService = new CompanyService(companyRepository);
        assertEquals(1, newInstanceService.getAllCompanies().size(), "A lista carregada do arquivo também deveria ter apenas 1 empresa.");
    }
}
