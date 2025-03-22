package sistemacompras.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import sistemacompras.model.Company;
import sistemacompras.model.Item;
import sistemacompras.model.Supplier;
import sistemacompras.service.CompanyService;
import sistemacompras.service.ItemService;
import sistemacompras.service.SupplierService;
import sistemacompras.util.DatabaseInitializer;
import sistemacompras.util.ExcelGenerator;
import sistemacompras.util.PDFGenerator;

public class MainFrame extends JFrame {
    private final ItemService itemService = new ItemService();
    private final CompanyService companyService = new CompanyService();
    private final SupplierService supplierService = new SupplierService();

    private JTextField descriptionField, codeField, brandField, priceField, quantityField;
    private JComboBox<String> supplierComboBox;
    private JList<String> itemList;
    private DefaultListModel<String> listModel;
    private JComboBox<String> statusFilterComboBox, supplierFilterComboBox;
    private JComboBox<String> exportSupplierComboBox, pdfSupplierComboBox;

    public MainFrame() {
        DatabaseInitializer.initialize();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sistema de Compras");
        setSize(1370, 727);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel de fundo com logo
        JPanel backgroundPanel = new BackgroundPanel("Logo.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel);

        // Painel esquerdo (inputs)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        leftPanel.add(new JLabel("Descrição:"));
        descriptionField = new JTextField(5);
        leftPanel.add(descriptionField);

        leftPanel.add(new JLabel("Código:"));
        codeField = new JTextField(5);
        leftPanel.add(codeField);

        leftPanel.add(new JLabel("Marca:"));
        brandField = new JTextField(5);
        leftPanel.add(brandField);

        leftPanel.add(new JLabel("Fornecedor:"));
        supplierComboBox = new JComboBox<>(supplierService.getAllSupplierNames().toArray(new String[0]));
        leftPanel.add(supplierComboBox);

        leftPanel.add(new JLabel("Preço:"));
        priceField = new JTextField(5);
        leftPanel.add(priceField);

        leftPanel.add(new JLabel("Quantidade:"));
        quantityField = new JTextField(5);
        leftPanel.add(quantityField);

        JButton addButton = new JButton("Adicionar Item");
        addButton.addActionListener(e -> addItem());
        leftPanel.add(addButton);

        JButton removeButton = new JButton("Remover Item");
        removeButton.addActionListener(e -> removeItem());
        leftPanel.add(removeButton);

        JButton purchasedButton = new JButton("Marcar como Comprado");
        purchasedButton.addActionListener(e -> markAsPurchased());
        leftPanel.add(purchasedButton);

        JButton partiallyPurchasedButton = new JButton("Marcar como Parcialmente Comprado");
        partiallyPurchasedButton.addActionListener(e -> markAsPartiallyPurchased());
        leftPanel.add(partiallyPurchasedButton);

        backgroundPanel.add(leftPanel, BorderLayout.WEST);

        // Lista de itens (centro)
        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        itemList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        updateItemList(itemService.getAllItems());
        JScrollPane scrollPane = new JScrollPane(itemList);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // Painel direito (filtros e ações)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);

        rightPanel.add(new JLabel("Filtrar por Status:"));
        statusFilterComboBox = new JComboBox<>(new String[]{"", "A Comprar", "Comprado", "Parcialmente Comprado"});
        rightPanel.add(statusFilterComboBox);

        rightPanel.add(new JLabel("Filtrar por Fornecedor:"));
        supplierFilterComboBox = new JComboBox<>(supplierService.getAllSupplierNames().toArray(new String[0]));
        rightPanel.add(supplierFilterComboBox);

        JButton filterButton = new JButton("Filtrar");
        filterButton.addActionListener(e -> filterItems());
        rightPanel.add(filterButton);

        rightPanel.add(new JLabel("Exportar Pedido Excel:"));
        exportSupplierComboBox = new JComboBox<>(supplierService.getAllSupplierNames().toArray(new String[0]));
        rightPanel.add(exportSupplierComboBox);

        rightPanel.add(new JLabel("Gerar Pedido PDF:"));
        pdfSupplierComboBox = new JComboBox<>(supplierService.getAllSupplierNames().toArray(new String[0]));
        rightPanel.add(pdfSupplierComboBox);

        JButton exportButton = new JButton("Exportar Excel");
        exportButton.addActionListener(e -> generateExcel());
        rightPanel.add(exportButton);

        JButton importButton = new JButton("Importar Excel");
        importButton.addActionListener(e -> importExcel());
        rightPanel.add(importButton);

        JButton pdfButton = new JButton("Gerar Pedido em PDF");
        pdfButton.addActionListener(e -> generatePDF());
        rightPanel.add(pdfButton);

        backgroundPanel.add(rightPanel, BorderLayout.EAST);

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu cadastroMenu = new JMenu("Cadastro");
        cadastroMenu.add(new JMenuItem("Cadastrar Empresa", 'E')).addActionListener(e -> openCompanyWindow());
        cadastroMenu.add(new JMenuItem("Alterar Empresa", 'A')).addActionListener(e -> alterCompany());
        cadastroMenu.add(new JMenuItem("Excluir Empresa", 'X')).addActionListener(e -> deleteCompany());
        cadastroMenu.add(new JMenuItem("Cadastrar Fornecedor", 'F')).addActionListener(e -> openSupplierWindow());
        cadastroMenu.add(new JMenuItem("Alterar Fornecedor", 'T')).addActionListener(e -> alterSupplier());
        cadastroMenu.add(new JMenuItem("Excluir Fornecedor", 'R')).addActionListener(e -> deleteSupplier());
        menuBar.add(cadastroMenu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    private void addItem() {
        String description = descriptionField.getText();
        String code = codeField.getText();
        String brand = brandField.getText();
        String supplier = (String) supplierComboBox.getSelectedItem();
        String priceText = priceField.getText();
        String quantityText = quantityField.getText();

        if (description.isEmpty() || code.isEmpty() || supplier == null || priceText.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) throw new NumberFormatException("Quantidade deve ser maior que zero!");

            Item item = new Item();
            item.setDescription(description);
            item.setCode(code);
            item.setBrand(brand.isEmpty() ? null : brand);
            item.setStatus("A Comprar");
            item.setQuantity(quantity);
            item.getSuppliersPrices().put(supplier, price);

            itemService.addItem(item);
            updateItemList(itemService.getAllItems());
            clearFields();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço ou quantidade inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeItem() {
        int[] selected = itemList.getSelectedIndices();
        if (selected.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int index : selected) {
            int id = itemService.getAllItems().get(index).getId();
            itemService.deleteItem(id);
        }
        updateItemList(itemService.getAllItems());
    }

    private void markAsPurchased() {
        int[] selectedIndices = itemList.getSelectedIndices();
        if (selectedIndices.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos um item!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int index : selectedIndices) {
            Item item = itemService.getAllItems().get(index);
            item.setStatus("Comprado");
            itemService.updateItem(item);
        }
        updateItemList(itemService.getAllItems());
    }
    
    private void markAsPartiallyPurchased() {
        int[] selectedIndices = itemList.getSelectedIndices();
        if (selectedIndices.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos um item!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int index : selectedIndices) {
            Item item = itemService.getAllItems().get(index);
            item.setStatus("Parcialmente Comprado");
            itemService.updateItem(item);
        }
        updateItemList(itemService.getAllItems());
    }
    private void filterItems() {
        String status = (String) statusFilterComboBox.getSelectedItem();
        String supplier = (String) supplierFilterComboBox.getSelectedItem();
        List<Item> items;
        if (status != null && !status.isEmpty() && supplier != null && !supplier.isEmpty()) {
            items = itemService.getItemsByStatusAndSupplier(status, supplier);
        } else if (status != null && !status.isEmpty()) {
            items = itemService.getItemsByStatus(status);
        } else if (supplier != null && !supplier.isEmpty()) {
            items = itemService.getItemsBySupplier(supplier);
        } else {
            items = itemService.getAllItems();
        }
        updateItemList(items);
    }

    private void generateExcel() {
        String supplier = (String) exportSupplierComboBox.getSelectedItem();
        List<Item> items = supplier != null && !supplier.isEmpty() ? itemService.getItemsBySupplier(supplier) : itemService.getAllItems();
        ExcelGenerator.generateExcel(items, supplier);
        JOptionPane.showMessageDialog(this, "Planilha gerada como 'itens.xlsx'", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void importExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o arquivo Excel");
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            ExcelGenerator.importExcel(filePath, itemService);
            updateItemList(itemService.getAllItems());
            JOptionPane.showMessageDialog(this, "Preços atualizados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void generatePDF() {
        String supplier = (String) pdfSupplierComboBox.getSelectedItem();
        Company company = companyService.getCompany();
        if (company == null) {
            JOptionPane.showMessageDialog(this, "Cadastre a empresa primeiro!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int[] selected = itemList.getSelectedIndices();
        if (selected.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos um item!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Item> items = new java.util.ArrayList<>();
        for (int index : selected) {
            items.add(itemService.getAllItems().get(index));
        }
        int orderNumber = itemService.getAllItems().size() + 1;
        PDFGenerator.generatePDF(items, company, supplier, orderNumber);
        JOptionPane.showMessageDialog(this, "Pedido gerado como 'pedido_" + orderNumber + ".pdf'", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openCompanyWindow() {
        JDialog dialog = new JDialog(this, "Cadastrar Empresa", true);
        dialog.setLayout(new GridLayout(4, 2));
        dialog.setSize(300, 200);

        JTextField nameField = new JTextField();
        JTextField cnpjField = new JTextField();
        JTextField buyerNameField = new JTextField();

        dialog.add(new JLabel("Nome:"));
        dialog.add(nameField);
        dialog.add(new JLabel("CNPJ:"));
        dialog.add(cnpjField);
        dialog.add(new JLabel("Comprador:"));
        dialog.add(buyerNameField);
        dialog.add(new JButton("Cadastrar") {{
            addActionListener(e -> {
                String name = nameField.getText();
                String cnpj = cnpjField.getText();
                String buyerName = buyerNameField.getText();
                if (name.isEmpty() || cnpj.isEmpty() || buyerName.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Company company = new Company();
                company.setName(name);
                company.setCnpj(cnpj);
                company.setBuyerName(buyerName);
                companyService.addCompany(company);
                JOptionPane.showMessageDialog(dialog, "Empresa cadastrada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            });
        }});

        dialog.setVisible(true);
    }

    private void alterCompany() {
        List<Company> companies = companyService.getAllCompanies();
        if (companies.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma empresa cadastrada!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String idStr = JOptionPane.showInputDialog(this, "Digite o ID da empresa a alterar:");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                Company company = companies.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
                if (company == null) {
                    JOptionPane.showMessageDialog(this, "Empresa não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String name = JOptionPane.showInputDialog(this, "Novo nome:", company.getName());
                String cnpj = JOptionPane.showInputDialog(this, "Novo CNPJ:", company.getCnpj());
                String buyerName = JOptionPane.showInputDialog(this, "Novo comprador:", company.getBuyerName());
                if (name != null && cnpj != null && buyerName != null) {
                    company.setName(name);
                    company.setCnpj(cnpj);
                    company.setBuyerName(buyerName);
                    companyService.updateCompany(company);
                    JOptionPane.showMessageDialog(this, "Empresa alterada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCompany() {
        List<Company> companies = companyService.getAllCompanies();
        if (companies.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma empresa cadastrada!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String idStr = JOptionPane.showInputDialog(this, "Digite o ID da empresa a excluir:");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                if (companies.stream().anyMatch(c -> c.getId() == id)) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Confirma a exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        companyService.deleteCompany(id);
                        JOptionPane.showMessageDialog(this, "Empresa excluída!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Empresa não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openSupplierWindow() {
        JDialog dialog = new JDialog(this, "Cadastrar Fornecedor", true);
        dialog.setLayout(new GridLayout(4, 2));
        dialog.setSize(300, 200);

        JTextField nameField = new JTextField();
        JTextField cnpjField = new JTextField();
        JTextField sellerNameField = new JTextField();

        dialog.add(new JLabel("Nome:"));
        dialog.add(nameField);
        dialog.add(new JLabel("CNPJ:"));
        dialog.add(cnpjField);
        dialog.add(new JLabel("Vendedor:"));
        dialog.add(sellerNameField);
        dialog.add(new JButton("Cadastrar") {{
            addActionListener(e -> {
                String name = nameField.getText();
                String cnpj = cnpjField.getText();
                String sellerName = sellerNameField.getText();
                if (name.isEmpty() || cnpj.isEmpty() || sellerName.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Supplier supplier = new Supplier();
                supplier.setName(name);
                supplier.setCnpj(cnpj);
                supplier.setSellerName(sellerName);
                supplierService.addSupplier(supplier);
                updateSupplierComboboxes();
                JOptionPane.showMessageDialog(dialog, "Fornecedor cadastrado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            });
        }});

        dialog.setVisible(true);
    }

    private void alterSupplier() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        if (suppliers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum fornecedor cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String idStr = JOptionPane.showInputDialog(this, "Digite o ID do fornecedor a alterar:");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                Supplier supplier = suppliers.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
                if (supplier == null) {
                    JOptionPane.showMessageDialog(this, "Fornecedor não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String name = JOptionPane.showInputDialog(this, "Novo nome:", supplier.getName());
                String cnpj = JOptionPane.showInputDialog(this, "Novo CNPJ:", supplier.getCnpj());
                String sellerName = JOptionPane.showInputDialog(this, "Novo vendedor:", supplier.getSellerName());
                if (name != null && cnpj != null && sellerName != null) {
                    supplier.setName(name);
                    supplier.setCnpj(cnpj);
                    supplier.setSellerName(sellerName);
                    supplierService.updateSupplier(supplier);
                    updateSupplierComboboxes();
                    JOptionPane.showMessageDialog(this, "Fornecedor alterado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSupplier() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        if (suppliers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum fornecedor cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String idStr = JOptionPane.showInputDialog(this, "Digite o ID do fornecedor a excluir:");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                if (suppliers.stream().anyMatch(s -> s.getId() == id)) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Confirma a exclusão?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        supplierService.deleteSupplier(id);
                        updateSupplierComboboxes();
                        JOptionPane.showMessageDialog(this, "Fornecedor excluído!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Fornecedor não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateItemList(List<Item> items) {
        listModel.clear();
        for (Item item : items) {
            listModel.addElement(item.toString());
            int index = listModel.size() - 1;
            switch (item.getStatus()) {
                case "A Comprar":
                    itemList.setForeground(Color.RED);
                    break;
                case "Comprado":
                    itemList.setForeground(Color.GREEN);
                    break;
                case "Parcialmente Comprado":
                    itemList.setForeground(Color.ORANGE);
                    break;
            }
        }
    }

    private void clearFields() {
        descriptionField.setText("");
        codeField.setText("");
        brandField.setText("");
        supplierComboBox.setSelectedIndex(0);
        priceField.setText("");
        quantityField.setText("");
    }

    private void updateSupplierComboboxes() {
        String[] suppliers = supplierService.getAllSupplierNames().toArray(new String[0]);
        supplierComboBox.setModel(new DefaultComboBoxModel<>(suppliers));
        supplierFilterComboBox.setModel(new DefaultComboBoxModel<>(suppliers));
        exportSupplierComboBox.setModel(new DefaultComboBoxModel<>(suppliers));
        pdfSupplierComboBox.setModel(new DefaultComboBoxModel<>(suppliers));
    }

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                System.err.println("Erro ao carregar imagem de fundo: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}