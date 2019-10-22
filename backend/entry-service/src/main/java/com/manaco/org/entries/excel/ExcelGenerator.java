package com.manaco.org.entries.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.manaco.org.dto.ProductDto;
import com.manaco.org.entries.ProductDetail;
import com.manaco.org.model.Item;
import com.manaco.org.model.Transaction;
import com.manaco.org.model.TransactionOption;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelGenerator {

    public static synchronized ByteArrayInputStream downloadTransation(List<Transaction> transactions, String identifier) throws IOException {
        String[] COLUMNs = { "ITEM", "TIPO", "FECHA", "INGRESO", "EGRESO", "CANTIDAD", "PRECIO_NETO", "PRECIO_ACTUAL",
                "UFV", "INGRESO_TOTAL", "EGRESO_TOTAL", "TOTAL", "TOTAL_ACT", "INCR", "SECCION", "CUENTA", "ALMACEN", "DESCRIPCION", "TRANS_TIPO"};
        if(TransactionOption.valueOf(identifier) == TransactionOption.REPUESTOS) {

        }

        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
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
            int cont = 0;
            for (Transaction transaction : transactions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(transaction.getItem().getId());
                row.createCell(1).setCellValue(transaction.getType().toString());
                row.createCell(2).setCellValue(transaction.getTransactionDate().toString());

                row.createCell(3).setCellValue((transaction.getEntry() == null) ? BigDecimal.ZERO.intValue():transaction.getEntry().doubleValue());
                row.createCell(4).setCellValue((transaction.getEgress() == null) ? BigDecimal.ZERO.intValue():transaction.getEgress().doubleValue());
                row.createCell(5).setCellValue((transaction.getBalance() == null)? BigDecimal.ZERO.intValue():transaction.getBalance().doubleValue());
                row.createCell(6).setCellValue((transaction.getPriceNeto() == null) ? BigDecimal.ZERO.intValue():transaction.getPriceNeto().doubleValue());
                row.createCell(7).setCellValue((transaction.getPriceActual() == null)? BigDecimal.ZERO.intValue():transaction.getPriceActual().doubleValue());
                row.createCell(8).setCellValue((transaction.getUfv() == null)? 0 : transaction.getUfv().getValue().doubleValue());
                row.createCell(9).setCellValue((transaction.getTotalEntry() == null)? BigDecimal.ZERO.intValue():transaction.getTotalEntry().doubleValue());
                row.createCell(10).setCellValue((transaction.getTotalEgress() == null)? BigDecimal.ZERO.intValue():transaction.getTotalEgress().doubleValue());
                row.createCell(11).setCellValue((transaction.getTotalNormal() == null)? BigDecimal.ZERO.intValue() :transaction.getTotalNormal().doubleValue());
                row.createCell(12).setCellValue((transaction.getTotalUpdate() == null)? BigDecimal.ZERO.intValue():transaction.getTotalUpdate().doubleValue());
                row.createCell(13).setCellValue((transaction.getIncrement()== null)? BigDecimal.ZERO.intValue():transaction.getIncrement().doubleValue());
                row.createCell(14).setCellValue((transaction.getInformation()== null)? "":transaction
                        .getInformation().get("SECCION_D"));
                row.createCell(15).setCellValue((transaction.getInformation()== null)? "":transaction
                        .getInformation().get("CUENTA"));
                row.createCell(16).setCellValue((transaction.getInformation()== null)? 0:Integer.parseInt(transaction
                        .getInformation().get("ALMACEN")));
                row.createCell(17).setCellValue((transaction.getInformation()== null)? "":transaction
                        .getInformation().get("DESCRIPCION"));
                row.createCell(18).setCellValue((transaction.getInformation()== null)? 0: Integer.parseInt(transaction
                        .getInformation().get("TRANS_TIPO")));
//                row.createCell(14).setCellValue(transaction.getEgress().toString());
//                System.out.println(transaction.getItem().getId());
                cont++;
                System.out.println(cont);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            System.out.println(e);
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
                row.createCell(1).setCellValue(item.getQuantity().doubleValue());
                row.createCell(2).setCellValue(item.getPrice().doubleValue());

                row.createCell(3).setCellValue(item.getLastUpdate().toString());
                row.createCell(4).setCellValue(item.getTotal().doubleValue());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public static ByteArrayInputStream downloadProducFinish(List<Transaction> transactions) {
        String[] COLUMNs = { "ITEM", "TIPO", "FECHA", "INGRESO", "EGRESO", "CANTIDAD", "PRECIO_NETO", "PRECIO_ACTUAL",
                "UFV", "INGRESO_TOTAL", "EGRESO_TOTAL", "TOTAL", "TOTAL_ACT", "INCR", "ALMACEN", "NRO_DOC", "SEMANA",
                "CAMINO", "TOTAL_GENERAL"};

        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
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

                row.createCell(3).setCellValue((transaction.getEntry() == null) ? BigDecimal.ZERO.intValue():transaction.getEntry().doubleValue());
                row.createCell(4).setCellValue((transaction.getEgress() == null) ? BigDecimal.ZERO.intValue():transaction.getEgress().doubleValue());
                row.createCell(5).setCellValue((transaction.getBalance() == null)? BigDecimal.ZERO.intValue():transaction.getBalance().doubleValue());
                row.createCell(6).setCellValue((transaction.getPriceNeto() == null) ? BigDecimal.ZERO.intValue():transaction.getPriceNeto().doubleValue());
                row.createCell(7).setCellValue((transaction.getPriceActual() == null)? BigDecimal.ZERO.intValue():transaction.getPriceActual().doubleValue());
                row.createCell(8).setCellValue((transaction.getUfv() == null)? 0 : transaction.getUfv().getValue().doubleValue());
                row.createCell(9).setCellValue((transaction.getTotalEntry() == null)? BigDecimal.ZERO.intValue():transaction.getTotalEntry().doubleValue());
                row.createCell(10).setCellValue((transaction.getTotalEgress() == null)? BigDecimal.ZERO.intValue():transaction.getTotalEgress().doubleValue());
                row.createCell(11).setCellValue((transaction.getTotalNormal() == null)? BigDecimal.ZERO.intValue() :transaction.getTotalNormal().doubleValue());
                row.createCell(12).setCellValue((transaction.getTotalUpdate() == null)? BigDecimal.ZERO.intValue():transaction.getTotalUpdate().doubleValue());
                row.createCell(13).setCellValue((transaction.getIncrement()== null)? BigDecimal.ZERO.intValue():transaction.getIncrement().doubleValue());
                row.createCell(14).setCellValue((transaction.getInformation()== null)? 0:Integer.parseInt(transaction
                        .getInformation().get("Almacen")));
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
       }
    }

    public synchronized ByteArrayInputStream downloadTransation1(List<Transaction> transactions, String identifier) throws IOException {
        String[] COLUMNs = { "ITEM", "TIPO", "FECHA", "INGRESO", "EGRESO", "CANTIDAD", "PRECIO_NETO", "PRECIO_ACTUAL",
                "UFV", "INGRESO_TOTAL", "EGRESO_TOTAL", "TOTAL", "TOTAL_ACT", "INCR", "SECCION", "CUENTA", "ALMACEN", "DESCRIPCION", "TRANS_TIPO"};

        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
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

                row.createCell(3).setCellValue((transaction.getEntry() == null) ? BigDecimal.ZERO.intValue():transaction.getEntry().doubleValue());
                row.createCell(4).setCellValue((transaction.getEgress() == null) ? BigDecimal.ZERO.intValue():transaction.getEgress().doubleValue());
                row.createCell(5).setCellValue((transaction.getBalance() == null)? BigDecimal.ZERO.intValue():transaction.getBalance().doubleValue());
                row.createCell(6).setCellValue((transaction.getPriceNeto() == null) ? BigDecimal.ZERO.intValue():transaction.getPriceNeto().doubleValue());
                row.createCell(7).setCellValue((transaction.getPriceActual() == null)? BigDecimal.ZERO.intValue():transaction.getPriceActual().doubleValue());
                row.createCell(8).setCellValue((transaction.getUfv() == null)? 0 : transaction.getUfv().getValue().doubleValue());
                row.createCell(9).setCellValue((transaction.getTotalEntry() == null)? BigDecimal.ZERO.intValue():transaction.getTotalEntry().doubleValue());
                row.createCell(10).setCellValue((transaction.getTotalEgress() == null)? BigDecimal.ZERO.intValue():transaction.getTotalEgress().doubleValue());
                row.createCell(11).setCellValue((transaction.getTotalNormal() == null)? BigDecimal.ZERO.intValue() :transaction.getTotalNormal().doubleValue());
                row.createCell(12).setCellValue((transaction.getTotalUpdate() == null)? BigDecimal.ZERO.intValue():transaction.getTotalUpdate().doubleValue());
                row.createCell(13).setCellValue((transaction.getIncrement()== null)? BigDecimal.ZERO.intValue():transaction.getIncrement().doubleValue());
//                row.createCell(14).setCellValue((transaction.getTransactionDetail()== null)? "":transaction.getTransactionDetail()
//                        .getInformation().get("SECCION_D"));
//                row.createCell(15).setCellValue((transaction.getTransactionDetail()== null)? "":transaction.getTransactionDetail()
//                        .getInformation().get("CUENTA"));
//                row.createCell(16).setCellValue((transaction.getTransactionDetail()== null)? 0:Integer.parseInt(transaction.getTransactionDetail()
//                        .getInformation().get("ALMACEN")));
//                row.createCell(17).setCellValue((transaction.getTransactionDetail()== null)? "":transaction.getTransactionDetail()
//                        .getInformation().get("DESCRIPCION"));
//                row.createCell(18).setCellValue((transaction.getTransactionDetail()== null)? 0: Integer.parseInt(transaction.getTransactionDetail()
//                        .getInformation().get("TRANS_TIPO")));
//                row.createCell(14).setCellValue(transaction.getEgress().toString());

            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public static synchronized ByteArrayInputStream downloadProductoTerminado(List<ProductDto> transactions, String identifier) throws IOException {
//        String[] COLUMNs = { "TIPO", "ALMACEN", "NRO_DOC", "ARTICULO", "PARES", "PCOSTO", "FECHA_DOC", "ORIGEN",
//                "SEMANA", "TABLA_ORIGEN", "TIPO_MOV"};
        String[] COLUMNs = { "FECHA", "ITEM", "PU_ACTUAL", "CANTIDAD", "TIPO", "ALMACEN", "NRO_DOC",
                "SEMANA", "TAB_ORIG", "TIPO_MOV"};


        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
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
            int cont = 0;
            for (ProductDto dto : transactions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getFECHA_DOC().toString());
                row.createCell(1).setCellValue(dto.getARTICULO());
                row.createCell(2).setCellValue(dto.getPCOSTO());
                row.createCell(3).setCellValue(dto.getPARES());
                row.createCell(4).setCellValue(dto.getTIPO());
                row.createCell(5).setCellValue(dto.getALMACEN());
                row.createCell(6).setCellValue(dto.getNRO_DOC());
                row.createCell(7).setCellValue(dto.getSEMANA());
                row.createCell(8).setCellValue(dto.getTABLA_ORIGEN());
                row.createCell(9).setCellValue(dto.getTIPO_MOV());
//
//                row.createCell(7).setCellValue(dto.getORIGEN());
//                row.createCell(9).setCellValue(dto.getTABLA_ORIGEN());
//                row.createCell(10).setCellValue(dto.getTIPO_MOV());

//                row.createCell(3).setCellValue((transaction.getEntry() == null) ? BigDecimal.ZERO.intValue():transaction.getEntry().doubleValue());

                cont++;
                System.out.println(cont);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }



    public static ByteArrayInputStream downloadDetail(List<ProductDetail> transactions) {
        String[] COLUMNs = { "DETALLE", "PARES_DEBER", "PARES_HABER"};

        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook(100);
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
            for (ProductDetail productDetail: transactions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(productDetail.getDetalle());
                if(productDetail.getType().equals("PARES_HABER")) {
                    row.createCell(2).setCellValue((productDetail.getQuantity() == null) ? BigDecimal.ZERO.intValue():productDetail.getQuantity().doubleValue());
                } else {
                    row.createCell(1).setCellValue((productDetail.getQuantity() == null) ? BigDecimal.ZERO.intValue():productDetail.getQuantity().doubleValue());
                }

//                row.createCell(2).setCellValue(transaction.getTransactionDate().toString());
//
//                row.createCell(3).setCellValue((transaction.getEntry() == null) ? BigDecimal.ZERO.intValue():transaction.getEntry().doubleValue());
//                row.createCell(4).setCellValue((transaction.getEgress() == null) ? BigDecimal.ZERO.intValue():transaction.getEgress().doubleValue());
//                row.createCell(5).setCellValue((transaction.getBalance() == null)? BigDecimal.ZERO.intValue():transaction.getBalance().doubleValue());
//                row.createCell(6).setCellValue((transaction.getPriceNeto() == null) ? BigDecimal.ZERO.intValue():transaction.getPriceNeto().doubleValue());
//                row.createCell(7).setCellValue((transaction.getPriceActual() == null)? BigDecimal.ZERO.intValue():transaction.getPriceActual().doubleValue());
//                row.createCell(8).setCellValue((transaction.getUfv() == null)? 0 : transaction.getUfv().getValue().doubleValue());
//                row.createCell(9).setCellValue((transaction.getTotalEntry() == null)? BigDecimal.ZERO.intValue():transaction.getTotalEntry().doubleValue());
//                row.createCell(10).setCellValue((transaction.getTotalEgress() == null)? BigDecimal.ZERO.intValue():transaction.getTotalEgress().doubleValue());
//                row.createCell(11).setCellValue((transaction.getTotalNormal() == null)? BigDecimal.ZERO.intValue() :transaction.getTotalNormal().doubleValue());
//                row.createCell(12).setCellValue((transaction.getTotalUpdate() == null)? BigDecimal.ZERO.intValue():transaction.getTotalUpdate().doubleValue());
//                row.createCell(13).setCellValue((transaction.getIncrement()== null)? BigDecimal.ZERO.intValue():transaction.getIncrement().doubleValue());
//                row.createCell(14).setCellValue((transaction.getInformation()== null)? 0:Integer.parseInt(transaction
//                        .getInformation().get("Almacen")));
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }
}
