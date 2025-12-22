package util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.JTable;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFExporter {
    
    public static void exportTableToPDF(JTable table, String title, String filePath) throws Exception {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();
        
        // Title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph titlePara = new Paragraph("HAPA Vehicle Rental System - " + title, titleFont);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        document.add(titlePara);
        
        // Date
        Font dateFont = new Font(Font.FontFamily.HELVETICA, 10);
        Paragraph datePara = new Paragraph("Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), dateFont);
        datePara.setAlignment(Element.ALIGN_CENTER);
        document.add(datePara);
        document.add(new Paragraph(" "));
        
        // Table
        PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
        pdfTable.setWidthPercentage(100);
        
        // Headers
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        for (int i = 0; i < table.getColumnCount(); i++) {
            PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), headerFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfTable.addCell(cell);
        }
        
        // Data
        Font dataFont = new Font(Font.FontFamily.HELVETICA, 9);
        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                Object value = table.getValueAt(row, col);
                String cellValue = value != null ? value.toString() : "";
                pdfTable.addCell(new Phrase(cellValue, dataFont));
            }
        }
        
        document.add(pdfTable);
        document.close();
    }
}