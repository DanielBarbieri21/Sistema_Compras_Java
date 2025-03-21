package sistemacompras.dao;

import sistemacompras.model.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {
    private static final String URL = "jdbc:sqlite:database.db";

    public void insert(Company company) {
        String sql = "INSERT INTO company (name, cnpj, buyer_name) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, company.getName());
            pstmt.setString(2, company.getCnpj());
            pstmt.setString(3, company.getBuyerName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir empresa: " + e.getMessage());
        }
    }

    public void update(Company company) {
        String sql = "UPDATE company SET name = ?, cnpj = ?, buyer_name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, company.getName());
            pstmt.setString(2, company.getCnpj());
            pstmt.setString(3, company.getBuyerName());
            pstmt.setInt(4, company.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar empresa: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM company WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao excluir empresa: " + e.getMessage());
        }
    }

    public Company getFirst() {
        String sql = "SELECT * FROM company LIMIT 1";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                Company company = new Company();
                company.setId(rs.getInt("id"));
                company.setName(rs.getString("name"));
                company.setCnpj(rs.getString("cnpj"));
                company.setBuyerName(rs.getString("buyer_name"));
                return company;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresa: " + e.getMessage());
        }
        return null;
    }

    public List<Company> getAll() {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM company";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Company company = new Company();
                company.setId(rs.getInt("id"));
                company.setName(rs.getString("name"));
                company.setCnpj(rs.getString("cnpj"));
                company.setBuyerName(rs.getString("buyer_name"));
                companies.add(company);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empresas: " + e.getMessage());
        }
        return companies;
    }
}