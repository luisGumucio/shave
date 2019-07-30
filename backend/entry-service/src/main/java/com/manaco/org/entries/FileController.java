package com.manaco.org.entries;

import com.manaco.org.model.FileUpload;
import com.manaco.org.model.Process;
import com.manaco.org.model.TransactionType;
import com.manaco.org.model.Ufv;
import com.manaco.org.repositories.TransactionRepository;
import com.manaco.org.repositories.UfvRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

@RestController
@RequestMapping("/files")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    private static final int INDEX = 0;

    @Autowired
    private FileService fileService;
    @Autowired
    private UfvRepository ufvRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @PostMapping
    public ResponseEntity<FileUpload> upload(@RequestParam("file") MultipartFile file,
                                             @RequestPart("option") String option, @RequestParam("process") String process) {
        LOGGER.info("adding new files");
        FileUpload fileUpload = null;
        if (file == null) {
            throw new MultipartException("You must select the a file for uploading");
        }
        try {
            Process processActive = fileService.saveProcess(option, process);
            fileService.readFile(file.getInputStream(), processActive);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(fileUpload, HttpStatus.OK);
    }

    @PostMapping(path = "/ufv")
    public void uploadUfv(@RequestParam("file") MultipartFile file) {
        if (file == null) {
            throw new MultipartException("You must select the a file for uploading");
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
//            executeUfv(workbook.getSheetAt(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeUfv(XSSFSheet sheet) {
        int day = 0;
        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                try {
                    switch (j) {
                        case 0:
                            day = (int) cell.getNumericCellValue();
                            break;
                        case 1:
                            saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 1);
                            break;
                        case 2:
                            if (!cell.getStringCellValue().equals("")) {
                                saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 2);
                            }
                            break;
                        case 3:
                            saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 3);
                            break;
                        case 4:
                            if (!cell.getStringCellValue().equals("")) {
                                saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 4);
                            }
                            break;
                        case 5:
                            saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 5);
                            break;
                        case 6:
                            if (!cell.getStringCellValue().equals("")) {
                                saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 6);
                            }
                            break;
                        case 7:
                            saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 7);
                            break;
                        case 8:
                            saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 8);
                            break;
                        case 9:
                            if (!cell.getStringCellValue().equals("")) {
                                saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 9);
                            }
                            break;
                        case 10:
                            saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 10);
                            break;
                        case 11:
                            if (!cell.getStringCellValue().equals("")) {
                                saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 11);
                            }
                            ;
                            break;
                        case 12:
                            saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 12);
                            break;
                    }

                } catch (IllegalStateException ex) {
                    LOGGER.error("Failed to convert to object", ex.getMessage());
                }
            }
        }
    }

    private void saveUfv(int day, BigDecimal numericCellValue, int month) {
        LocalDate date = LocalDate.of(2018, month, day);
        Ufv ufv = ufvRepository.findByCreationDate(date);
        if (ufv == null) {
            ufv = new Ufv();
            ufv.setCreationDate(LocalDate.of(2018, month, day));
            ufv.setValue(numericCellValue);
            ufvRepository.save(ufv);
        }
    }
}
