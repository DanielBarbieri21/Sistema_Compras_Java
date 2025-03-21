package sistemacompras.service;

import sistemacompras.dao.CompanyDAO;
import sistemacompras.model.Company;

import java.util.List;

public class CompanyService {
    private final CompanyDAO companyDAO = new CompanyDAO();

    public void addCompany(Company company) {
        companyDAO.insert(company);
    }

    public void updateCompany(Company company) {
        companyDAO.update(company);
    }

    public void deleteCompany(int id) {
        companyDAO.delete(id);
    }

    public Company getCompany() {
        return companyDAO.getFirst();
    }

    public List<Company> getAllCompanies() {
        return companyDAO.getAll();
    }
}