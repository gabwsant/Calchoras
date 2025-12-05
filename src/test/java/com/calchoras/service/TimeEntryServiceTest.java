package com.calchoras.service;

import com.calchoras.model.TimeEntry;
import com.calchoras.repository.TimeEntryRepository;
import com.calchoras.service.interfaces.ITimeEntryService;
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
import static org.mockito.Mockito.*;

@DisplayName("Testes para TimeEntryService")
class TimeEntryServiceTest {

    private final String TEST_FILE_PATH = "batidas_teste.json";
    private TimeEntryService timeEntryService;
    private TimeEntryRepository timeEntryRepository;
    private IEmployeeService employeeServiceMock;

    @BeforeEach
    void setUp() {
        deleteTestFile();
        timeEntryRepository = new TimeEntryRepository(TEST_FILE_PATH);

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

        TimeEntry entry = new TimeEntry();
        entry.setEmployeeId(1);
        entry.setEntryDate(LocalDate.of(2025, 11, 29));
        entry.setClockIn(LocalTime.of(9, 0));

        TimeEntry saved = timeEntryService.save(entry);

        assertNotNull(saved, "O objeto salvo não deveria ser nulo");
        assertEquals(1, saved.getId(), "O ID da primeira batida deveria ser 1");

        List<TimeEntry> entries = timeEntryService.findByEmployeeId(1);
        assertEquals(1, entries.size(), "Deveria ter uma batida de ponto registrada");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar batida duplicada")
    void save_ShouldThrowExceptionOnDuplicate() {
        when(employeeServiceMock.existsById(1)).thenReturn(true);

        TimeEntry entry = new TimeEntry();
        entry.setEmployeeId(1);
        entry.setEntryDate(LocalDate.of(2025, 11, 29));
        entry.setClockIn(LocalTime.of(9, 0));

        timeEntryService.save(entry);

        TimeEntry duplicate = new TimeEntry();
        duplicate.setEmployeeId(1);
        duplicate.setEntryDate(LocalDate.of(2025, 11, 29));
        duplicate.setClockIn(LocalTime.of(10, 0));

        assertThrows(IllegalArgumentException.class,
                () -> timeEntryService.save(duplicate),
                "Deveria lançar exceção ao tentar adicionar batida duplicada");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar adicionar batida para funcionário inexistente")
    void save_ShouldThrowExceptionForNonExistentEmployee() {
        when(employeeServiceMock.existsById(999)).thenReturn(false);

        TimeEntry entry = new TimeEntry();
        entry.setEmployeeId(999);
        entry.setEntryDate(LocalDate.of(2025, 11, 29));
        entry.setClockIn(LocalTime.of(9, 0));

        assertThrows(IllegalArgumentException.class,
                () -> timeEntryService.save(entry),
                "Deveria lançar exceção para funcionário inexistente");
    }

    @Test
    @DisplayName("Deve atualizar uma batida existente")
    void update_ShouldModifyExistingEntry() {
        when(employeeServiceMock.existsById(1)).thenReturn(true);

        TimeEntry entry = new TimeEntry();
        entry.setEmployeeId(1);
        entry.setEntryDate(LocalDate.of(2025, 11, 29));
        entry.setClockIn(LocalTime.of(9, 0));

        TimeEntry saved = timeEntryService.save(entry);

        saved.setClockIn(LocalTime.of(10, 0));
        timeEntryService.update(saved);

        Optional<TimeEntry> loaded = timeEntryService.findById(saved.getId());
        assertTrue(loaded.isPresent(), "A batida atualizada deveria existir");
        assertEquals(LocalTime.of(10, 0), loaded.get().getClockIn(), "O horário deveria ser atualizado");
    }

    @Test
    @DisplayName("Deve deletar uma batida pelo employeeId e data")
    void deleteByEmployeeIdAndDate_ShouldRemoveEntry() {
        when(employeeServiceMock.existsById(1)).thenReturn(true);

        TimeEntry entry = new TimeEntry();
        entry.setEmployeeId(1);
        entry.setEntryDate(LocalDate.of(2025, 11, 29));
        entry.setClockIn(LocalTime.of(9, 0));

        timeEntryService.save(entry);

        timeEntryService.deleteByEmployeeIdAndDate(1, LocalDate.of(2025, 11, 29));

        List<TimeEntry> entries = timeEntryService.findByEmployeeId(1);
        assertEquals(0, entries.size(), "A lista de batidas deveria estar vazia");
    }
}
