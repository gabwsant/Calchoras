package com.calchoras.service;

import com.calchoras.model.Employee;
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

        Employee newEmployee = new Employee(
                1,
                "João da Silva",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60
        );

        Employee addedEmployee = employeeService.save(newEmployee);

        assertNotNull(addedEmployee);
        assertEquals(1, addedEmployee.getId());
        assertEquals("João da Silva", addedEmployee.getName());
        assertEquals(1, employeeService.findAll().size());
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

        employeeService.save(newEmployee);

        EmployeeService newInstance =
                new EmployeeService(new EmployeeRepository(TEST_FILE_PATH), companyServiceMock);

        List<Employee> loadedEmployees = newInstance.findAll();
        assertEquals(1, loadedEmployees.size());
        assertEquals("Maria Oliveira", loadedEmployees.getFirst().getName());

        List<Employee> loadedEmployeesByCompany =  newInstance.findByCompanyId(1);
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

        Employee added = employeeService.save(original);

        added.setName("Carlos Pereira (Editado)");
        added.setShiftOut(LocalTime.of(17, 30));

        employeeService.update(added);

        Optional<Employee> updatedOpt = employeeService.findById(added.getId());

        assertTrue(updatedOpt.isPresent());
        assertEquals("Carlos Pereira (Editado)", updatedOpt.get().getName());
        assertEquals(LocalTime.of(17, 30), updatedOpt.get().getShiftOut());
    }

    @Test
    @DisplayName("Não deve permitir atualizar funcionário duplicando nome dentro da mesma empresa")
    void update_ShouldThrowException_WhenNameAlreadyExistsInCompany() {
        Employee emp1 = new Employee(1,
                "Carlos Pereira",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60);
        Employee emp2 = new Employee(1,
                "Gabriel",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60);

        employeeService.save(emp1);
        emp2 = employeeService.save(emp2);

        emp2.setName("Carlos Pereira");

        Employee finalEmp = emp2;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeeService.update(finalEmp),
                "Deveria lançar exceção ao tentar duplicar nome");

        assertEquals("Outro funcionário com esse nome já existe para a empresa.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve permitir atualizar funcionário quando não há conflito de nome")
    void update_ShouldSucceed_WhenNoDuplicateName() {
        Employee emp = new Employee(1,
                "Carlos Pereira",
                LocalTime.of(8, 0),
                LocalTime.of(17, 0),
                60);
        emp = employeeService.save(emp);

        emp.setName("Charles");
        Employee updated = employeeService.update(emp);

        Optional<Employee> loaded = employeeService.findById(updated.getId());
        assertTrue(loaded.isPresent());
        assertEquals("Charles", loaded.get().getName(), "Nome deveria ser atualizado corretamente");
    }

    @Test
    @DisplayName("Deve remover um funcionário da lista e do arquivo")
    void deleteEmployee_ShouldRemoveEmployee() {
        Employee emp1 = employeeService.save(
                new Employee(1, "Ana", LocalTime.of(8,0), LocalTime.of(17,0), 60)
        );

        Employee emp2 = employeeService.save(
                new Employee(1, "Beto", LocalTime.of(8,0), LocalTime.of(17,0), 60)
        );

        assertEquals(2, employeeService.findAll().size());

        employeeService.deleteById(emp1.getId());

        assertEquals(1, employeeService.findAll().size());
        assertFalse(employeeService.findById(emp1.getId()).isPresent());
        assertTrue(employeeService.findById(emp2.getId()).isPresent());

        EmployeeService newInstance =
                new EmployeeService(new EmployeeRepository(TEST_FILE_PATH), companyServiceMock);
        assertEquals(1, newInstance.findAll().size());
    }
}
