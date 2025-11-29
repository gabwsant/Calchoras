package com.calchoras.service;

import com.calchoras.model.Employee;
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

        // MOCK do serviço de empresas
        companyServiceMock = Mockito.mock(ICompanyService.class);

        // Toda empresa passada nos testes EXISTE (para não dar erro no addEmployee)
        when(companyServiceMock.exists(anyInt())).thenReturn(true);

        employeeService = new EmployeeService(TEST_FILE_PATH, companyServiceMock);
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
        assertEquals(0, employeeService.getAllEmployees().size());

        Employee newEmployee = new Employee(
                1,
                "João da Silva",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60
        );

        Employee addedEmployee = employeeService.addEmployee(newEmployee);

        assertNotNull(addedEmployee);
        assertEquals(1, addedEmployee.getId());
        assertEquals("João da Silva", addedEmployee.getName());
        assertEquals(1, employeeService.getAllEmployees().size());
    }

    @Test
    @DisplayName("Deve persistir o funcionário no arquivo após adicionar")
    void addEmployee_ShouldPersistDataToFile() {
        Employee newEmployee = new Employee(
                1,
                "Maria Oliveira",
                LocalTime.of(9, 0),
                LocalTime.of(18, 0),
                60
        );

        employeeService.addEmployee(newEmployee);

        // cria nova instância para forçar leitura do arquivo
        EmployeeService newInstance = new EmployeeService(TEST_FILE_PATH, companyServiceMock);

        // getAllEmployees
        List<Employee> loadedEmployees = newInstance.getAllEmployees();
        assertEquals(1, loadedEmployees.size());
        assertEquals("Maria Oliveira", loadedEmployees.getFirst().getName());

        //getEmployeesByCompany
        List<Employee> loadedEmployeesByCompany =  newInstance.getEmployeesByCompany(1);
        assertEquals(1, loadedEmployeesByCompany.size());
        assertEquals(1, loadedEmployeesByCompany.getFirst().getCompanyId());
    }

    @Test
    @DisplayName("Deve atualizar os dados de um funcionário existente")
    void updateEmployee_ShouldModifyExistingEmployee() {
        Employee original = new Employee(
                1,
                "Carlos Pereira",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60
        );

        Employee added = employeeService.addEmployee(original);

        // edita
        added.setName("Carlos Pereira (Editado)");
        added.setShiftOut(LocalTime.of(17, 30));

        employeeService.updateEmployee(added);

        Optional<Employee> updatedOpt = employeeService.getEmployeeById(added.getId());

        assertTrue(updatedOpt.isPresent());
        assertEquals("Carlos Pereira (Editado)", updatedOpt.get().getName());
        assertEquals(LocalTime.of(17, 30), updatedOpt.get().getShiftOut());
    }

    @Test
    @DisplayName("Deve remover um funcionário da lista e do arquivo")
    void deleteEmployee_ShouldRemoveEmployee() {
        Employee emp1 = employeeService.addEmployee(
                new Employee(1, "Ana", LocalTime.of(8,0), LocalTime.of(17,0), 60)
        );

        Employee emp2 = employeeService.addEmployee(
                new Employee(1, "Beto", LocalTime.of(8,0), LocalTime.of(17,0), 60)
        );

        assertEquals(2, employeeService.getAllEmployees().size());

        employeeService.deleteEmployee(emp1.getId());

        assertEquals(1, employeeService.getAllEmployees().size());
        assertFalse(employeeService.getEmployeeById(emp1.getId()).isPresent());
        assertTrue(employeeService.getEmployeeById(emp2.getId()).isPresent());

        // verifica persistência
        EmployeeService newInstance = new EmployeeService(TEST_FILE_PATH, companyServiceMock);
        assertEquals(1, newInstance.getAllEmployees().size());
    }
}
