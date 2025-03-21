package sistemacompras.service;

import sistemacompras.dao.ItemDAO;
import sistemacompras.model.Item;

import java.util.List;

public class ItemService {
    private final ItemDAO itemDAO = new ItemDAO();

    public void addItem(Item item) {
        itemDAO.insert(item);
    }

    public void updateItem(Item item) {
        itemDAO.update(item);
    }

    public void deleteItem(int id) {
        itemDAO.delete(id);
    }

    public List<Item> getAllItems() {
        return itemDAO.getAll();
    }

    public List<Item> getItemsByStatus(String status) {
        return itemDAO.getByStatus(status);
    }

    public List<Item> getItemsBySupplier(String supplier) {
        return itemDAO.getBySupplier(supplier);
    }

    public List<Item> getItemsByStatusAndSupplier(String status, String supplier) {
        return itemDAO.getByStatusAndSupplier(status, supplier);
    }
}