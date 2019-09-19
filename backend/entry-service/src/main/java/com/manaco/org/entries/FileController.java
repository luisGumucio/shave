package com.manaco.org.entries;

import com.manaco.org.entries.excel.ExcelGenerator;
import com.manaco.org.model.*;
import com.manaco.org.model.Process;
import com.manaco.org.repositories.ItemRepository;
import com.manaco.org.repositories.TransactionRepository;
import com.manaco.org.repositories.UfvRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private ItemRepository itemRepository;

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
            fileService.readFile(file.getInputStream(), processActive, option);
            fileUpload = fileService.saveFile(option, file.getOriginalFilename(), fileService.getTotal());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(fileUpload, HttpStatus.OK);
    }

    @PostMapping(path = "/ufv")
    public void uploadUfv(@RequestParam("file") MultipartFile file, @RequestParam String year) {
        if (file == null) {
            throw new MultipartException("You must select the a file for uploading");
        }

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            executeUfv(workbook.getSheetAt(0), year);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @GetMapping(value = "/download/{identifer}/{type}")
    public ResponseEntity<InputStreamResource> excelCustomersReport(@PathVariable("identifer") String identifier,
                                                                    @PathVariable("type") String type) throws IOException {

        if(type.equals("transaction")) {
            return downloadTransaction(identifier);
        }
        return downloadItems(identifier);
    }

    private ResponseEntity<InputStreamResource> downloadTransaction(String identifier) throws IOException {
        List<Transaction> transactions = transactionRepository.findAllByIdentifier(identifier);

        ByteArrayInputStream in = ExcelGenerator.downloadTransation(transactions, identifier);
        // return IOUtils.toByteArray(in);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    private ResponseEntity<InputStreamResource> downloadItems(String identifier) throws IOException  {
        List<Item> items = itemRepository.findAllByIdentifier(identifier);

        ByteArrayInputStream in = ExcelGenerator.downloadItem(items);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    @GetMapping
    public Page<FileUpload> get(@RequestParam(defaultValue = "0") int page) {
        return fileService.getFiles(PageRequest.of(page, 10));
    }


    private void executeUfv(XSSFSheet sheet, String year) {
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
                                if (!cell.getStringCellValue().equals("")) {
                                    saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 1, year);
                                }
                                break;
                            case 2:
                                if (!cell.getStringCellValue().equals("")) {
                                    saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 2, year);
                                }
                                break;
                            case 3:
                                if (!cell.getStringCellValue().equals("")) {
                                    saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 3, year);
                                }
                                break;
                            case 4:
                                if (!cell.getStringCellValue().equals("")) {
                                    saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 4, year);
                                }
                                break;
                            case 5:
                                if (!cell.getStringCellValue().equals("")) {
                                    saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 5, year);
                                }
                                break;
                            case 6:
                                if (!cell.getStringCellValue().equals("")) {
                                    saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 6, year);
                                }
                                break;
                            case 7:
                                if (!cell.getStringCellValue().equals("")) {
                                saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 7, year);
                                   }
                                break;
                            case 8:
                                if (!cell.getStringCellValue().equals("")) {
                                    saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 8, year);
                                }
                                break;
                            case 9:
                                if (!cell.getStringCellValue().equals("")) {
                                    saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 9, year);
                                }
                                break;
                            case 10:
                                if (!cell.getStringCellValue().equals("")) {
                                    saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 10, year);
                                }
                                break;
                            case 11:
                                if (!cell.getStringCellValue().equals("")) {
                                    saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 11, year);
                                }
                                break;
                            case 12:
                                if (!cell.getStringCellValue().equals("")) {
                                saveUfv(day, new BigDecimal(cell.getStringCellValue().replace(",", ".")), 12, year);
                                }
                                break;
                        }

                } catch (IllegalStateException ex) {
                    LOGGER.error("Failed to convert to object", ex.getMessage());
                }
            }
        }
    }

    private void saveUfv(int day, BigDecimal numericCellValue, int month, String year) {
        int years = Integer.parseInt(year);
        LocalDate date = LocalDate.of(years, month, day);
        Ufv ufv = ufvRepository.findByCreationDate(date);
        if (ufv == null) {
            ufv = new Ufv();
            ufv.setCreationDate(LocalDate.of(years, month, day));
            ufv.setValue(numericCellValue);
            ufvRepository.save(ufv);
        }
    }
}
