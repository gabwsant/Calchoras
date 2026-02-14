package com.calchoras.repository;

import com.calchoras.exception.DatabaseException;
import com.calchoras.model.Employee;
import com.calchoras.repository.interfaces.IEmployeeRepository;
import com.calchoras.util.SQLiteConnection;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteEmployeeRepository implements IEmployeeRepository {

    public SQLiteEmployeeRepository() {
        String sql = "CREATE TABLE IF NOT EXISTS Employee (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "company_id INTEGER NOT NULL, " +
                "name TEXT NOT NULL, " +
                "shift_in TEXT, " +
                "shift_out TEXT, " +
                "lunch_break_minutes INTEGER, " +
                "active INTEGER DEFAULT 1, " +
                "FOREIGN KEY (company_id) REFERENCES Company(id) ON DELETE CASCADE);";

        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inicializar tabela Employee: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";

        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao recuperar empregados: " + e.getMessage(), e);
        }
        return employees;
    }

    @Override
    public Optional<Employee> findById(int id) {
        String sql = "SELECT * FROM Employee WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEmployee(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao recuperar empregado: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Employee> findByCompanyId(int companyId) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE company_id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao recuperar empregados da empresa: " + e.getMessage(), e);
        }
        return employees;
    }

    @Override
    public List<Employee> findActivesByCompanyId(int companyId) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE company_id = ? AND active = 1";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao recuperar empregados ativos da empresa: " + e.getMessage(), e);
        }
        return employees;
    }

    @Override
    public Employee save(Employee employee) {
        String sql = "INSERT INTO Employee (company_id, name, shift_in, shift_out, lunch_break_minutes, active) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, employee.getCompanyId());
            pstmt.setString(2, employee.getName());
            pstmt.setString(3, employee.getShiftIn().toString());
            pstmt.setString(4, employee.getShiftOut().toString());
            pstmt.setLong(5, employee.getLunchBreakMinutes());
            pstmt.setInt(6, employee.isActive() ? 1 : 0);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    employee.setId(rs.getInt(1));
                }
            }
            return employee;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao salvar o empregado: " + employee.getName(), e);
        }
    }

    @Override
    public Employee update(Employee employee) {
        String sql = "UPDATE Employee SET company_id = ?, name = ?, shift_in = ?, shift_out = ?, lunch_break_minutes = ?, active = ? WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, employee.getCompanyId());
            pstmt.setString(2, employee.getName());
            pstmt.setString(3, employee.getShiftIn().toString());
            pstmt.setString(4, employee.getShiftOut().toString());
            pstmt.setLong(5, employee.getLunchBreakMinutes());
            pstmt.setInt(6, employee.isActive() ? 1 : 0);
            pstmt.setInt(7, employee.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar o empregado: " + employee.getName(), e);
        }

        return employee;
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM Employee WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar o empregado: " + id, e);
        }
    }

    @Override
    public boolean disableById(int id) {
        String sql = "UPDATE Employee SET active = 0 WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao desabilitar empregado: " + id, e);
        }
    }

    @Override
    public boolean enableById(int id) {
        String sql = "UPDATE Employee SET active = 1 WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao habilitar empregado: " + id, e);
        }
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM Employee WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar existencia do empregado: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM Employee WHERE name = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar existencia do empregado: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByCompanyId(int companyId) {
        String sql = "SELECT 1 FROM Employee WHERE company_id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar existencia de empregado para a empresa: " + e.getMessage(), e);
        }
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getInt("company_id"),
                rs.getString("name"),
                LocalTime.parse(rs.getString("shift_in")),
                LocalTime.parse(rs.getString("shift_out")),
                rs.getLong("lunch_break_minutes"),
                rs.getInt("active") == 1
        );
    }
}