package com.calchoras.service;

import com.calchoras.dto.TimeEntryDTO;
import com.calchoras.repository.TimeEntryRepository;
import com.calchoras.service.interfaces.IEmployeeService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@DisplayName("Testes para TimeEntryService")
class TimeEntryServiceTest {

    private final String TEST_FILE_PATH = "batidas_teste.json";
    private TimeEntryService timeEntryService;
    private IEmployeeService employeeServiceMock;

    @BeforeEach
    void setUp() {
        deleteTestFile();

        TimeEntryRepository timeEntryRepository = new TimeEntryRepository(TEST_FILE_PATH);

        employeeServiceMock = Mockito.mock(IEmployeeService.class);
        when(employeeServiceMock.existsById(anyInt())).thenReturn(true);

        timeEntryService = new TimeEntryService(timeEntryRepository, employeeServiceMock);
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
    @DisplayName("Deve adicionar uma batida de ponto e atribuir ID")
    void save_ShouldAssignIdAndPersist() {
        when(employeeServiceMock.existsById(1)).thenReturn(true);

        TimeEntryDTO entry = new TimeEntryDTO(
                0,
                1,
                LocalDate.of(2025, 11, 29),
                LocalTime.of(9, 0),
                null, null, null,
                false
        );

        TimeEntryDTO saved = timeEntryService.save(entry);

        assertNotNull(saved, "O objeto salvo não deveria ser nulo");
        assertTrue(saved.id() > 0, "O ID da batida deveria ser gerado (> 0)");

        List<TimeEntryDTO> entries = timeEntryService.findByEmployeeId(1);
        assertEquals(1, entries.size(), "Deveria ter uma batida de ponto registrada");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar batida duplicada (mesmo funcionário e data)")
    void save_ShouldThrowExceptionOnDuplicate() {
        when(employeeServiceMock.existsById(1)).thenReturn(true);

        TimeEntryDTO entry = new TimeEntryDTO(
                0,
                1,
                LocalDate.of(2025, 11, 29),
                LocalTime.of(9, 0),
                null, null, null, false
        );

        timeEntryService.save(entry);

        // Tentativa de salvar outro registro para o mesmo dia
        TimeEntryDTO duplicate = new TimeEntryDTO(
                0,
                1,
                LocalDate.of(2025, 11, 29), // Mesma data
                LocalTime.of(10, 0),       // Horário diferente, mas data igual bloqueia
                null, null, null, false
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> timeEntryService.save(duplicate),
                "Deveria lançar exceção ao tentar adicionar batida duplicada");

        assertEquals("Já existe um lançamento para esse funcionário nessa data.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar adicionar batida para funcionário inexistente")
    void save_ShouldThrowExceptionForNonExistentEmployee() {
        when(employeeServiceMock.existsById(999)).thenReturn(false);

        TimeEntryDTO entry = new TimeEntryDTO(
                0,
                999,
                LocalDate.of(2025, 11, 29),
                LocalTime.of(9, 0),
                null, null, null, false
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> timeEntryService.save(entry),
                "Deveria lançar exceção para funcionário inexistente");

        assertEquals("Funcionário não encontrado.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar uma batida existente")
    void update_ShouldModifyExistingEntry() {
        when(employeeServiceMock.existsById(1)).thenReturn(true);

        // 1. Cria e Salva
        TimeEntryDTO original = new TimeEntryDTO(
                0,
                1,
                LocalDate.of(2025, 11, 29),
                LocalTime.of(9, 0),
                null, null, null, false
        );
        TimeEntryDTO saved = timeEntryService.save(original);

        // 2. Cria objeto de atualização (Mesmo ID, horário alterado)
        TimeEntryDTO updateRequest = new TimeEntryDTO(
                saved.id(), // ID retornado pelo save
                1,
                LocalDate.of(2025, 11, 29),
                LocalTime.of(10, 0), // Horário alterado
                null, null, null, false
        );

        // 3. Executa Update
        timeEntryService.update(updateRequest);

        // 4. Verifica
        Optional<TimeEntryDTO> loaded = timeEntryService.findById(saved.id());
        assertTrue(loaded.isPresent(), "A batida atualizada deveria existir");
        assertEquals(LocalTime.of(10, 0), loaded.get().clockIn(), "O horário deveria ser atualizado");
    }

    @Test
    @DisplayName("Deve deletar uma batida pelo employeeId e data")
    void deleteByEmployeeIdAndDate_ShouldRemoveEntry() {
        when(employeeServiceMock.existsById(1)).thenReturn(true);

        TimeEntryDTO entry = new TimeEntryDTO(
                0,
                1,
                LocalDate.of(2025, 11, 29),
                LocalTime.of(9, 0),
                null, null, null, false
        );

        timeEntryService.save(entry);

        // Verifica que salvou
        assertEquals(1, timeEntryService.findByEmployeeId(1).size());

        // Deleta
        boolean deleted = timeEntryService.deleteByEmployeeIdAndDate(1, LocalDate.of(2025, 11, 29));

        assertTrue(deleted);
        List<TimeEntryDTO> entries = timeEntryService.findByEmployeeId(1);
        assertEquals(0, entries.size(), "A lista de batidas deveria estar vazia");
    }
}