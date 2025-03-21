package sistemacompras.dao;

import sistemacompras.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private static final String URL = "jdbc:sqlite:database.db";

    public void insert(Supplier supplier) {
        String sql = "INSERT INTO suppliers (name, cnpj, seller_name) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getCnpj());
            pstmt.setString(3, supplier.getSellerName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir fornecedor: " + e.getMessage());
        }
    }

    public void update(Supplier supplier) {
        String sql = "UPDATE suppliers SET name = ?, cnpj = ?, seller_name = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getCnpj());
            pstmt.setString(3, supplier.getSellerName());
            pstmt.setInt(4, supplier.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar fornecedor: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM suppliers WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao excluir fornecedor: " + e.getMessage());
        }
    }

    public List<Supplier> getAll() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setId(rs.getInt("id"));
                supplier.setName(rs.getString("name"));
                supplier.setCnpj(rs.getString("cnpj"));
                supplier.setSellerName(rs.getString("seller_name"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar fornecedores: " + e.getMessage());
        }
        return suppliers;
    }

    public List<String> getAllNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT name FROM suppliers";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar nomes de fornecedores: " + e.getMessage());
        }
        return names;
    }
}