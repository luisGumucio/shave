package com.manaco.org.entries.processator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class Data {

    private List<ItemTemp> datas;

    public Data() {
        datas = new ArrayList<>();
    }

    public void load() {
        File file = null;

        try {
            file = new ClassPathResource(
                    "costo.xlsx").getFile();
            FileInputStream excelFile = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                ItemTemp temp = new ItemTemp();
                for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    switch (j) {
                        case 0:
                            temp.setItem(cell.getStringCellValue());
                            break;
                        case 1:
                            temp.setCosto(new BigDecimal(cell.getNumericCellValue()));
                            break;
                    }
                }
                datas.add(temp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ItemTemp filter(String id) {
        return datas.stream().filter(b -> b.getItem().equals(id)).findFirst().orElse(null);
    }
}
