package com.calchoras.service;

import com.calchoras.dto.EmployeeDTO;
import com.calchoras.repository.EmployeeRepository;
import com.calchoras.service.interfaces.ICompanyService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Testes para o EmployeeService")
class EmployeeServiceTest {

    private final String TEST_FILE_PATH = "funcionarios_teste.json";
    private EmployeeService employeeService;
    private ICompanyService companyServiceMock;

    @BeforeEach
    void setUp() {
        deleteTestFile();
        EmployeeRepository employeeRepository = new EmployeeRepository(TEST_FILE_PATH);

        companyServiceMock = Mockito.mock(ICompanyService.class);
        when(companyServiceMock.existsById(anyInt())).thenReturn(true);

        employeeService = new EmployeeService(employeeRepository, companyServiceMock);
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
    @DisplayName("Deve adicionar um funcionário, atribuir um ID e aumentar a lista")
    void addEmployee_ShouldAssignIdAndIncreaseListSize() {
        assertEquals(0, employeeService.findAll().size());

        EmployeeDTO newEmployee = new EmployeeDTO(
                0,
                1,
                "João da Silva",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60,
                true
        );

        EmployeeDTO addedEmployee = employeeService.save(newEmployee);

        assertNotNull(addedEmployee);
        assertTrue(addedEmployee.id() > 0, "O ID deve ser gerado e maior que 0");
        assertEquals("João da Silva", addedEmployee.name());
        assertEquals(1, employeeService.findAll().size());
    }

    @Test
    @DisplayName("Deve persistir o funcionário no arquivo após adicionar")
    void addEmployee_ShouldPersistDataToFile() {
        EmployeeDTO newEmployee = new EmployeeDTO(
                0,
                1,
                "Maria Oliveira",
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                60,
                true
        );

        employeeService.save(newEmployee);

        EmployeeService newInstance =
                new EmployeeService(new EmployeeRepository(TEST_FILE_PATH), companyServiceMock);

        List<EmployeeDTO> loadedEmployees = newInstance.findAll();
        assertEquals(1, loadedEmployees.size());
        assertEquals("Maria Oliveira", loadedEmployees.getFirst().name());

        List<EmployeeDTO> loadedEmployeesByCompany = newInstance.findByCompanyId(1);
        assertEquals(1, loadedEmployeesByCompany.size());
        assertEquals(1, loadedEmployeesByCompany.getFirst().companyId());
    }

    @Test
    @DisplayName("Deve atualizar os dados de um funcionário existente")
    void updateEmployee_ShouldModifyExistingEmployee() {
        EmployeeDTO original = new EmployeeDTO(
                0,
                1,
                "Carlos Pereira",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60,
                true
        );
        EmployeeDTO saved = employeeService.save(original);

        EmployeeDTO updateRequest = new EmployeeDTO(
                saved.id(),
                1,
                "Carlos Pereira (Editado)",
                LocalTime.of(8, 0),
                LocalTime.of(17, 30),
                60,
                true
        );

        employeeService.update(updateRequest);

        Optional<EmployeeDTO> updatedOpt = employeeService.findById(saved.id());

        assertTrue(updatedOpt.isPresent());
        assertEquals("Carlos Pereira (Editado)", updatedOpt.get().name());
        assertEquals(LocalTime.of(17, 30), updatedOpt.get().shiftOut());
    }

    @Test
    @DisplayName("Não deve permitir atualizar funcionário duplicando nome dentro da mesma empresa")
    void update_ShouldThrowException_WhenNameAlreadyExistsInCompany() {
        EmployeeDTO emp1 = employeeService.save(new EmployeeDTO(0, 1, "Carlos Pereira", LocalTime.of(8, 0), LocalTime.of(17, 0), 60, true));
        EmployeeDTO emp2 = employeeService.save(new EmployeeDTO(0, 1, "Gabriel", LocalTime.of(8, 0), LocalTime.of(17, 0), 60, true));

        EmployeeDTO duplicateNameRequest = new EmployeeDTO(
                emp2.id(),
                1,
                "Carlos Pereira",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60,
                true
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeeService.update(duplicateNameRequest),
                "Deveria lançar exceção ao tentar duplicar nome");

        assertEquals("Já existe outro funcionário com este nome nesta empresa.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve permitir atualizar funcionário quando não há conflito de nome")
    void update_ShouldSucceed_WhenNoDuplicateName() {
        EmployeeDTO emp = employeeService.save(new EmployeeDTO(0, 1, "Carlos Pereira", LocalTime.of(8, 0), LocalTime.of(17, 0), 60, true));

        EmployeeDTO updateRequest = new EmployeeDTO(
                emp.id(),
                1,
                "Charles",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60,
                true
        );

        EmployeeDTO updated = employeeService.update(updateRequest);

        Optional<EmployeeDTO> loaded = employeeService.findById(updated.id());
        assertTrue(loaded.isPresent());
        assertEquals("Charles", loaded.get().name(), "Nome deveria ser atualizado corretamente");
    }

    @Test
    @DisplayName("Deve remover um funcionário da lista e do arquivo")
    void deleteEmployee_ShouldRemoveEmployee() {
        EmployeeDTO emp1 = employeeService.save(
                new EmployeeDTO(0, 1, "Ana", LocalTime.of(8, 0), LocalTime.of(17, 0), 60, true)
        );

        EmployeeDTO emp2 = employeeService.save(
                new EmployeeDTO(0, 1, "Beto", LocalTime.of(8, 0), LocalTime.of(17, 0), 60, true)
        );

        assertEquals(2, employeeService.findAll().size());

        boolean deleted = employeeService.deleteById(emp1.id());

        assertTrue(deleted);
        assertEquals(1, employeeService.findAll().size());
        assertFalse(employeeService.findById(emp1.id()).isPresent());
        assertTrue(employeeService.findById(emp2.id()).isPresent());

        EmployeeService newInstance =
                new EmployeeService(new EmployeeRepository(TEST_FILE_PATH), companyServiceMock);
        assertEquals(1, newInstance.findAll().size());
    }
}