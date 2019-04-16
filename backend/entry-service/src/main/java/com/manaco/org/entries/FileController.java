package com.manaco.org.entries;

import com.manaco.org.common.model.FileUpload;
import com.manaco.org.common.model.Proccess;
import com.manaco.org.common.model.TransactionOption;
import com.manaco.org.common.model.Ufv;
import com.manaco.org.common.repositories.ProccessRepository;
import com.manaco.org.common.repositories.UfvRepository;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
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
@CrossOrigin(origins = "*")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    private static final int INDEX = 0;
    @Autowired
    private FileService fileService;
    @Autowired
    private UfvRepository ufvRepository;
    @Autowired
    private ProccessRepository proccessRepository;

    @PostMapping
    public ResponseEntity<FileUpload> upload(@RequestParam("file") MultipartFile file,
            @RequestPart("option") String option, @RequestParam("process") String process) {
        LOGGER.info("adding new files");
        FileUpload fileUpload;
        if (file == null) {
            throw new MultipartException("You must select the a file for uploading");
        }
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            fileUpload = new FileUpload(file.getOriginalFilename(), TransactionOption.valueOf(option));
            Proccess proccess = getProcess(Integer.parseInt(process), fileUpload.getOption());
            fileService.readFile(workbook.getSheetAt(INDEX), fileUpload.getOption(), proccess.getId(),
                    proccess.getNumberProcess());
        } catch (IOException | OLE2NotOfficeXmlFileException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            executeUfv(workbook.getSheetAt(0));
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
        Ufv ufv = new Ufv();
        ufv.setCreationDate(LocalDate.of(2018, month, day));
        ufv.setValue(numericCellValue);
        ufvRepository.save(ufv);
    }

    private Proccess getProcess(int numberProcess, TransactionOption option) {
        Proccess process = proccessRepository.findByNumberProcessAndActiveIn(numberProcess, true);
        if (process == null) {
            return proccessRepository.save(new Proccess(numberProcess, option));
        }
        if (process.getType() != option && process.getNumberProcess() == numberProcess) {
            return proccessRepository.save(new Proccess(numberProcess, option));
        }
        return process;
    }
}
