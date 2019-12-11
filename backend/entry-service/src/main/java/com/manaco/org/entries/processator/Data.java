package com.manaco.org.entries.processator;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.manaco.org.entries.excel.ExcelReader;
import com.manaco.org.entries.excel.ExcelSheetCallback;
import com.manaco.org.entries.excel.ExcelWorkSheetRowCallbackHandler;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class Data {

    private List<ItemTemp> datas;

    public Data() {
        datas = new ArrayList<>();
    }

    public void load() {
        File file = null;
//
//        try {
//            file = new ClassPathResource(
//                    "costo1.xlsx").getFile();
//            FileInputStream excelFile = new FileInputStream(file);
//            Workbook workbook = new XSSFWorkbook(excelFile);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                ItemTemp temp = new ItemTemp();
//                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
//                    Cell cell = row.getCell(j);
//                    switch (j) {
//                        case 0:
//                            temp.setItem(cell.getStringCellValue());
//                            break;
//                        case 1:
//                            temp.setCosto(new BigDecimal(cell.getNumericCellValue()));
//                            break;
//                    }
//                }
//                datas.add(temp);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        OPCPackage pkg = null;
        FileInputStream excelFile = null;
        try {
            ExcelWorkSheetRowCallbackHandler sheetRowCallbackHandler =
                    new ExcelWorkSheetRowCallbackHandler((rowNum, map) -> {
                        ItemTemp temp = new ItemTemp();
                        temp.setItem(map.get("item"));
                        temp.setCosto(new BigDecimal(map.get("costo")));
                        datas.add(temp);
                    });

            file = new ClassPathResource(
                    "costo1.xlsx").getFile();
            excelFile = new FileInputStream(file);
            pkg = OPCPackage.open(excelFile);
            ExcelSheetCallback sheetCallback = new ExcelSheetCallback() {
                private int sheetNumber = 0;

                @Override
                public void startSheet(int sheetNum, String sheetName) {
                    this.sheetNumber = sheetNum;
                    System.out.println("Started processing sheet number=" + sheetNumber
                            + " and Sheet Name is '" + sheetName + "'");
                }

                @Override
                public void endSheet() {
                    System.out.println("Processing completed for sheet number=" + sheetNumber);
                }
            };
            ExcelReader excelReader = new ExcelReader(pkg, sheetRowCallbackHandler, sheetCallback);
            excelReader.process();

        } catch (RuntimeException are) {
//            LOGGER.error(are.getMessage(), are.getCause());
        } catch (InvalidFormatException ife) {
//            LOGGER.error(ife.getMessage(), ife.getCause());
        } catch (IOException ioe) {
//            LOGGER.error(ioe.getMessage(), ioe.getCause());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(excelFile);
            try {
                if (null != pkg) {
                    pkg.close();
                }
            } catch (IOException e) {
                // just ignore IO exception
            }
        }





    }

    public ItemTemp filter(String id) {
        return datas.stream().filter(b -> b.getItem().equals(id)).findFirst().orElse(null);
    }
}
