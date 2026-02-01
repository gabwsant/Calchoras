package com.calchoras.repository;

import com.calchoras.exception.DatabaseException;
import com.calchoras.model.Company;
import com.calchoras.repository.interfaces.ICompanyRepository;
import com.calchoras.util.SQLiteConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteCompanyRepository implements ICompanyRepository {

    public SQLiteCompanyRepository() {
        String sql = "CREATE TABLE IF NOT EXISTS Company (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT UNIQUE);";

        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inicializar tabela Company: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Company> findAll() {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM Company";

        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                companies.add(mapResultSetToCompany(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao recuperar empresas: " + e.getMessage(), e);
        }
        return companies;
    }

    @Override
    public Optional<Company> findById(int id) {
        String sql = "SELECT * FROM Company WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCompany(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao recuperar empresa por ID: " + id, e);
        }

        return Optional.empty();
    }

    @Override
    public Company save(Company company) {
        String sql = "INSERT INTO Company (name) VALUES (?)";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, company.getName());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    company.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Não foi possível salvar a empresa: " + company.getName(), e);
        }
        return company;
    }

    @Override
    public Company update(Company company) {
        String sql = "UPDATE Company SET name = ? WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, company.getName());
            pstmt.setInt(2, company.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar empresa: " + e.getMessage(), e);
        }
        return company;
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM Company WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao deletar empresa: " + id, e);
        }
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM Company WHERE id = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar existência de empresa: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM Company WHERE name = ?";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar existência de empresa por nome: " + e.getMessage(), e);
        }
    }

    private Company mapResultSetToCompany(ResultSet rs) throws SQLException {
        return new Company(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}