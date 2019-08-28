package com.manaco.org.entries.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.manaco.org.model.Item;
import com.manaco.org.model.Transaction;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelGenerator {

    public static synchronized ByteArrayInputStream downloadTransation(List<Transaction> transactions) throws IOException {
//        String[] COLUMNs = {"ID", "Cantidad", "Precio", "FechaActualizacion"};
        String[] COLUMNs = { "ITEM", "TIPO", "FECHA", "INGRESO", "EGRESO", "CANTIDAD", "PRECIO_NETO", "PRECIO_ACTUAL",
        "UFV", "INGRESO_TOTAL", "EGRESO_TOTAL", "TOTAL", "TOTAL_ACT", "INCR"};
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("Repuestos");

            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            // CellStyle for Age
            CellStyle ageCellStyle = workbook.createCellStyle();
            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

            int rowIdx = 1;
            for (Transaction transaction : transactions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(transaction.getItem().getId());
                row.createCell(1).setCellValue(transaction.getType().toString());
                row.createCell(2).setCellValue(transaction.getTransactionDate().toString());

                row.createCell(3).setCellValue((transaction.getEntry() == null) ? "0":transaction.getEntry().toString());
                row.createCell(4).setCellValue((transaction.getEgress() == null) ? "0":transaction.getEgress().toString());
                row.createCell(5).setCellValue((transaction.getBalance() == null)? "0":transaction.getBalance().toString());
                row.createCell(6).setCellValue((transaction.getPriceNeto() == null) ? "0":transaction.getPriceNeto().toString());
                row.createCell(7).setCellValue((transaction.getPriceActual() == null)? "0":transaction.getPriceActual().toString());
                row.createCell(8).setCellValue(transaction.getUfv().getValue().toString());
                row.createCell(9).setCellValue((transaction.getTotalEntry() == null)? "0":transaction.getTotalEntry().toString());
                row.createCell(10).setCellValue((transaction.getTotalEgress() == null)? "0":transaction.getTotalEgress().toString());
                row.createCell(11).setCellValue((transaction.getTotalNormal() == null)? "0":transaction.getTotalNormal().toString());
                row.createCell(12).setCellValue((transaction.getTotalUpdate() == null)? "0":transaction.getTotalUpdate().toString());
                row.createCell(13).setCellValue((transaction.getIncrement()== null)? "0":transaction.getIncrement().toString());
//                row.createCell(14).setCellValue(transaction.getEgress().toString());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public static ByteArrayInputStream downloadItem(List<Item> items) {
        String[] COLUMNs = {"ID", "Cantidad", "Precio", "FechaActualizacion", "Total"};
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CreationHelper createHelper = workbook.getCreationHelper();

            Sheet sheet = workbook.createSheet("Repuestos");

            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            // Row for Header
            Row headerRow = sheet.createRow(0);

            // Header
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            // CellStyle for Age
            CellStyle ageCellStyle = workbook.createCellStyle();
            ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));

            int rowIdx = 1;
            for (Item item : items) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.getId());
                row.createCell(1).setCellValue(item.getQuantity().toString());
                row.createCell(2).setCellValue(item.getPrice().toString());

                row.createCell(3).setCellValue(item.getLastUpdate().toString());
                row.createCell(4).setCellValue(item.getTotal().toString());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }
}
