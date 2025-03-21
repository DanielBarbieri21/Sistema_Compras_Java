package sistemacompras.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sistemacompras.model.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemDAO {
    private static final String URL = "jdbc:sqlite:database.db";
    private final Gson gson = new Gson();

    public void insert(Item item) {
        String sql = "INSERT INTO items (description, code, brand, status, quantity, suppliers_prices) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getDescription());
            pstmt.setString(2, item.getCode());
            pstmt.setString(3, item.getBrand());
            pstmt.setString(4, item.getStatus());
            pstmt.setInt(5, item.getQuantity());
            pstmt.setString(6, gson.toJson(item.getSuppliersPrices()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir item: " + e.getMessage());
        }
    }

    public void update(Item item) {
        String sql = "UPDATE items SET description = ?, code = ?, brand = ?, status = ?, quantity = ?, suppliers_prices = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getDescription());
            pstmt.setString(2, item.getCode());
            pstmt.setString(3, item.getBrand());
            pstmt.setString(4, item.getStatus());
            pstmt.setInt(5, item.getQuantity());
            pstmt.setString(6, gson.toJson(item.getSuppliersPrices()));
            pstmt.setInt(7, item.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar item: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao excluir item: " + e.getMessage());
        }
    }

    public List<Item> getAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setDescription(rs.getString("description"));
                item.setCode(rs.getString("code"));
                item.setBrand(rs.getString("brand"));
                item.setStatus(rs.getString("status"));
                item.setQuantity(rs.getInt("quantity"));
                String suppliersPricesJson = rs.getString("suppliers_prices");
                Map<String, Double> suppliersPrices = gson.fromJson(suppliersPricesJson, new TypeToken<Map<String, Double>>() {}.getType());
                item.setSuppliersPrices(suppliersPrices);
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar itens: " + e.getMessage());
        }
        return items;
    }

    public List<Item> getByStatus(String status) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE status = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setDescription(rs.getString("description"));
                item.setCode(rs.getString("code"));
                item.setBrand(rs.getString("brand"));
                item.setStatus(rs.getString("status"));
                item.setQuantity(rs.getInt("quantity"));
                String suppliersPricesJson = rs.getString("suppliers_prices");
                Map<String, Double> suppliersPrices = gson.fromJson(suppliersPricesJson, new TypeToken<Map<String, Double>>() {}.getType());
                item.setSuppliersPrices(suppliersPrices);
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar itens por status: " + e.getMessage());
        }
        return items;
    }

    public List<Item> getBySupplier(String supplier) {
        List<Item> items = getAll();
        List<Item> filtered = new ArrayList<>();
        for (Item item : items) {
            if (item.getSuppliersPrices().containsKey(supplier)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    public List<Item> getByStatusAndSupplier(String status, String supplier) {
        List<Item> items = getByStatus(status);
        List<Item> filtered = new ArrayList<>();
        for (Item item : items) {
            if (item.getSuppliersPrices().containsKey(supplier)) {
                filtered.add(item);
            }
        }
        return filtered;
    }
}