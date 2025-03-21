package sistemacompras.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import sistemacompras.model.Company;
import sistemacompras.model.Item;

import java.io.FileOutputStream;
import java.util.List;

public class PDFGenerator {
    public static void generatePDF(List<Item> items, Company company, String supplier, int orderNumber) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("pedido_" + orderNumber + ".pdf"));
            document.open();
            document.add(new Paragraph("Pedido #" + orderNumber));
            document.add(new Paragraph("Empresa: " + company.getName() + " - CNPJ: " + company.getCnpj()));
            document.add(new Paragraph("Comprador: " + company.getBuyerName()));
            document.add(new Paragraph(" "));

            for (Item item : items) {
                StringBuilder line = new StringBuilder();
                line.append("I: ").append(item.getDescription())
                        .append("  Cdg: ").append(item.getCode())
                        .append("  M: ").append(item.getBrand() != null ? item.getBrand() : "N/A")
                        .append("  Qtd: ").append(item.getQuantity());
                line.append("  Prç: ");
                item.getSuppliersPrices().forEach((s, p) -> {
                    if (supplier == null || supplier.isEmpty() || s.equals(supplier)) {
                        line.append(s).append(": R$").append(String.format("%.2f", p)).append(", ");
                    }
                });
                document.add(new Paragraph(line.toString()));
            }
            document.close();
        } catch (Exception e) {
            System.err.println("Erro ao gerar PDF: " + e.getMessage());
        }
    }
}