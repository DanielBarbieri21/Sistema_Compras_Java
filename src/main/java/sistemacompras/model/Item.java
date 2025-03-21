package sistemacompras.model;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private int id;
    private String description;
    private String code;
    private String brand;
    private String status;
    private int quantity;
    private Map<String, Double> suppliersPrices;

    public Item() {
        this.suppliersPrices = new HashMap<>();
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Map<String, Double> getSuppliersPrices() { return suppliersPrices; }
    public void setSuppliersPrices(Map<String, Double> suppliersPrices) { this.suppliersPrices = suppliersPrices; }

    @Override
    public String toString() {
        return String.format("Desc: %s | Código: %s | Marca: %s | Status: %s | Qtd: %d | Forn/Preço: %s",
                description, code, brand != null ? brand : "N/A", status, quantity, suppliersPrices);
    }
}