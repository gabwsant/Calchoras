package com.calchoras.repository;

import com.calchoras.model.TimeEntry;
import com.calchoras.repository.interfaces.ITimeEntryRepository;
import com.calchoras.exception.DatabaseException;
import com.calchoras.util.SQLiteConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteTimeEntryRepository implements ITimeEntryRepository {

    public SQLiteTimeEntryRepository() {
        // 1. Criamos a tabela com as colunas exatas do seu Model
        String sqlTable = "CREATE TABLE IF NOT EXISTS TimeEntry (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "employee_id INTEGER NOT NULL, " +
                "entry_date TEXT NOT NULL, " +
                "clock_in TEXT, " +
                "lunch_in TEXT, " +
                "lunch_out TEXT, " +
                "clock_out TEXT, " +
                "day_off INTEGER DEFAULT 0, " +
                "FOREIGN KEY (employee_id) REFERENCES employee(id)" +
                ");";

        String sqlIndex = "CREATE INDEX IF NOT EXISTS idx_timeentry_emp_date ON TimeEntry(employee_id, entry_date);";

        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlTable);
            stmt.execute(sqlIndex);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inicializar tabela TimeEntry", e);
        }
    }

    @Override
    public List<TimeEntry> findAll() {
        List<TimeEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM TimeEntry";

        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToTimeEntry(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar todas as batidas", e);
        }
        return list;
    }

    @Override
    public Optional<TimeEntry> findById(int id) {
        String sql = "SELECT * FROM TimeEntry WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTimeEntry(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar batida por ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<TimeEntry> findByEmployeeId(int employeeId) {
        List<TimeEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM TimeEntry WHERE employee_id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, employeeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTimeEntry(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar batidas do funcionário: " + employeeId, e);
        }
        return list;
    }

    @Override
    public Optional<TimeEntry> findByEmployeeIdAndDate(int employeeId, LocalDate date) {
        String sql = "SELECT * FROM TimeEntry WHERE employee_id = ? AND entry_date = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, employeeId);
            pstmt.setString(2, date.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToTimeEntry(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar batida por data", e);
        }
        return Optional.empty();
    }

    @Override
    public TimeEntry save(TimeEntry entry) {
        String sql = "INSERT INTO TimeEntry (employee_id, entry_date, clock_in, lunch_in, lunch_out, clock_out, day_off) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, entry.getEmployeeId());
            pstmt.setString(2, entry.getEntryDate().toString());

            pstmt.setString(3, entry.getClockIn() != null ? entry.getClockIn().toString() : null);
            pstmt.setString(4, entry.getLunchIn() != null ? entry.getLunchIn().toString() : null);
            pstmt.setString(5, entry.getLunchOut() != null ? entry.getLunchOut().toString() : null);
            pstmt.setString(6, entry.getClockOut() != null ? entry.getClockOut().toString() : null);

            pstmt.setInt(7, entry.isDayOff() ? 1 : 0);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entry.setId(rs.getInt(1));
                }
            }
            return entry;

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar batida de ponto", e);
        }
    }

    @Override
    public TimeEntry update(TimeEntry entry) {
        String sql = "UPDATE TimeEntry SET entry_date = ?, clock_in = ?, lunch_in = ?, lunch_out = ?, clock_out = ?, day_off = ? " +
                "WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entry.getEntryDate().toString());
            pstmt.setString(2, entry.getClockIn() != null ? entry.getClockIn().toString() : null);
            pstmt.setString(3, entry.getLunchIn() != null ? entry.getLunchIn().toString() : null);
            pstmt.setString(4, entry.getLunchOut() != null ? entry.getLunchOut().toString() : null);
            pstmt.setString(5, entry.getClockOut() != null ? entry.getClockOut().toString() : null);
            pstmt.setInt(6, entry.isDayOff() ? 1 : 0);

            pstmt.setInt(7, entry.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DatabaseException("Nenhuma batida encontrada para atualizar com ID: " + entry.getId(), null);
            }
            return entry;

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar batida de ponto", e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM TimeEntry WHERE id = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar batida por ID", e);
        }
    }

    @Override
    public boolean deleteByEmployeeIdAndDate(int employeeId, LocalDate date) {
        String sql = "DELETE FROM TimeEntry WHERE employee_id = ? AND entry_date = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, employeeId);
            pstmt.setString(2, date.toString());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar batida por data", e);
        }
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM TimeEntry WHERE id = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar existência", e);
        }
    }

    @Override
    public boolean existsByEmployeeIdAndDate(int employeeId, LocalDate date) {
        String sql = "SELECT 1 FROM TimeEntry WHERE employee_id = ? AND entry_date = ?";
        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, date.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar existência por data", e);
        }
    }

    private TimeEntry mapResultSetToTimeEntry(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int empId = rs.getInt("employee_id");

        LocalDate date = LocalDate.parse(rs.getString("entry_date"));

        LocalTime clockIn = parseTime(rs.getString("clock_in"));
        LocalTime lunchIn = parseTime(rs.getString("lunch_in"));
        LocalTime lunchOut = parseTime(rs.getString("lunch_out"));
        LocalTime clockOut = parseTime(rs.getString("clock_out"));

        boolean dayOff = rs.getInt("day_off") == 1;

        return new TimeEntry(id, empId, date, clockIn, lunchIn, lunchOut, clockOut, dayOff);
    }

    private LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        return LocalTime.parse(timeStr);
    }
}