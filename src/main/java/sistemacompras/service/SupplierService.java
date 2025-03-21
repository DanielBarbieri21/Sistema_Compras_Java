package sistemacompras.service;

import sistemacompras.dao.SupplierDAO;
import sistemacompras.model.Supplier;

import java.util.List;

public class SupplierService {
    private final SupplierDAO supplierDAO = new SupplierDAO();

    public void addSupplier(Supplier supplier) {
        supplierDAO.insert(supplier);
    }

    public void updateSupplier(Supplier supplier) {
        supplierDAO.update(supplier);
    }

    public void deleteSupplier(int id) {
        supplierDAO.delete(id);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAll();
    }

    public List<String> getAllSupplierNames() {
        return supplierDAO.getAllNames();
    }
}