package sistemacompras.util;

import sistemacompras.model.Item;
import sistemacompras.service.ItemService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map.Entry;

public class ExcelGenerator {
    public static void generateExcel(List<Item> items, String supplier) {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Itens");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Descrição");
            header.createCell(1).setCellValue("Código");
            header.createCell(2).setCellValue("Marca");
            header.createCell(3).setCellValue("Fornecedor");
            header.createCell(4).setCellValue("Preço");
            header.createCell(5).setCellValue("Quantidade");
            header.createCell(6).setCellValue("Status");

            int rowNum = 1;
            for (Item item : items) {
                for (Entry<String, Double> entry : item.getSuppliersPrices().entrySet()) {
                    if (supplier == null || supplier.isEmpty() || entry.getKey().equals(supplier)) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(item.getDescription());
                        row.createCell(1).setCellValue(item.getCode());
                        row.createCell(2).setCellValue(item.getBrand() != null ? item.getBrand() : "N/A");
                        row.createCell(3).setCellValue(entry.getKey());
                        row.createCell(4).setCellValue(String.format("R$%.2f", entry.getValue()));
                        row.createCell(5).setCellValue(item.getQuantity());
                        row.createCell(6).setCellValue(item.getStatus());
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream("itens.xlsx")) {
                wb.write(fos);
            }
        } catch (Exception e) {
            System.err.println("Erro ao gerar Excel: " + e.getMessage());
        }
    }

    public static void importExcel(String filePath, ItemService itemService) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String description = row.getCell(0).getStringCellValue();
                String code = row.getCell(1).getStringCellValue();
                String brand = row.getCell(2).getStringCellValue();
                String supplier = row.getCell(3).getStringCellValue();
                String priceStr = row.getCell(4).getStringCellValue().replace("R$", "").replace(",", ".");
                double price = Double.parseDouble(priceStr);

                List<Item> items = itemService.getAllItems();
                for (Item item : items) {
                    if (item.getDescription().equals(description) && item.getCode().equals(code) &&
                            (item.getBrand() == null ? "N/A" : item.getBrand()).equals(brand)) {
                        item.getSuppliersPrices().put(supplier, price);
                        itemService.updateItem(item);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao importar Excel: " + e.getMessage());
        }
    }
}