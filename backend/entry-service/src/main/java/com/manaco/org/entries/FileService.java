package com.manaco.org.entries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manaco.org.entries.excel.ExcelReader;
import com.manaco.org.entries.excel.ExcelSheetCallback;
import com.manaco.org.entries.excel.ExcelWorkSheetRowCallbackHandler;
import com.manaco.org.entries.processator.ProcesatorInitial;
import com.manaco.org.model.*;
import com.manaco.org.model.Process;
import com.manaco.org.utils.ProcesatorObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ProcessService processService;

    @Autowired
    ProcesatorInitial processatorInitial;

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Void> readFile(InputStream file, Process processActive) {

        switch (processActive.getTransactionOption()) {
            case SALDO_INITIAL_PRIMA:
                initialExecute(file, processatorInitial, TransactionOption.PRIMA, processActive);
                break;
            case SALDO_INITIAL_REPUESTOS:
                initialExecute(file, processatorInitial, TransactionOption.SALDO_INITIAL_REPUESTOS, processActive);
                break;
            case SALDO_INITIAL_PRODUCTO:
                initialExecute(file, processatorInitial, TransactionOption.PRODUCTO, processActive);
                break;
        }
        return null;
    }
//
    public void initialExecute(InputStream file, ProcesatorObject procesatorObject, TransactionOption option, Process processActive) {
        OPCPackage pkg = null;
        try {
            ExcelWorkSheetRowCallbackHandler sheetRowCallbackHandler =
                    new ExcelWorkSheetRowCallbackHandler((rowNum, map) -> {
                        LOGGER.info("rowNum=" + rowNum + ", map=" + map);
                        procesatorObject.execute(map, option, processActive);
                    });
            pkg = OPCPackage.open(file);
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
            LOGGER.error(are.getMessage(), are.getCause());
        } catch (InvalidFormatException ife) {
            LOGGER.error(ife.getMessage(), ife.getCause());
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage(), ioe.getCause());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(file);
            try {
                if (null != pkg) {
                    pkg.close();
                }
            } catch (IOException e) {
                // just ignore IO exception
            }
        }
    }
//
//    private void executeReplacement(XSSFSheet sheet, int process, int numberProcess) {
//        List<Transaction> raws = new ArrayList<>();
//        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
//            raws.add(executeReplacement(sheet.getRow(i), process));
//        }
//
//        Collections.sort(raws);
//        executeTransaction(raws);
//    }
//
//    private void executeMaterial(XSSFSheet sheet, int process, int numberProcess) {
//        if (numberProcess == 1) {
//            List<Transaction> raws = new ArrayList<>();
//            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
//                raws.add(executeMaterial(sheet.getRow(i), process));
//            }
//
//            Collections.sort(raws);
//            System.out.println(raws.get(0).getTransactionDate());
//            executeTransaction(raws);
//        } else if (numberProcess == 2) {
//            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
//                executeSecondProcess(sheet.getRow(i));
//            }
//        }
//
//    }
//
//    private void executeSecondProcess(Row row) {
//        Item item = new Item();
//        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
//            Cell cell = row.getCell(j);
//            try {
//                switch (j) {
//                    case 0:
//                        item.setId(cell.getStringCellValue());
//                        break;
//                    case 1:
//                        item.setPrice(new BigDecimal(cell.getNumericCellValue()));
//                        break;
//                }
//
//            } catch (IllegalStateException ex) {
//                System.out.print(ex);
//            }
//        }
//        HttpEntity<Item> request = new HttpEntity(item);
////        restTemplate.postForObject(URL + "/item", request, Item.class);
//        System.out.println(item.getId());
//    }
//
//    private Transaction executeMaterial(Row row, int process) {
//        Transaction transaction = new Transaction();
//        Item item = new Item();
//        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
//            Cell cell = row.getCell(j);
//            try {
//                switch (j) {
//                    case 0:
//                        transaction.setTransactionDate(cell.getDateCellValue()
//                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//                        break;
//                    case 1:
//                        item.setId(cell.getStringCellValue());
//                        break;
//                    case 2:
//                        if (cell.getStringCellValue().equals("E")) {
//                            transaction.setType(TransactionType.ENTRY);
//                        } else if (cell.getStringCellValue().equals("S")) {
//                            transaction.setType(TransactionType.EGRESS);
//
//                        }
//                        break;
//                    case 3:
//                        transaction.setQuantity(new BigDecimal(cell.getNumericCellValue()));
//                        break;
//                    case 5:
//                        transaction.setPriceNeto(new BigDecimal(cell.getNumericCellValue()));
//                        break;
//                    case 7:
////                        detail.setSeccion(Integer.parseInt(cell.getStringCellValue()));
//                        break;
//                    case 8:
////                        detail.setNroAccount(cell.getStringCellValue());
//                        break;
//                    case 9:
////                        detail.setDescription(cell.getStringCellValue());
//                        break;
//                    case 10:
////                        detail.setWareHouseId(Integer.parseInt(cell.getStringCellValue()));
//                        break;
//                }
//            } catch (IllegalStateException ex) {
//                System.out.print(ex);
//            }
//        }
////        detail.setItem(item);
////        transaction.setTransactionDetail(detail);
////        transaction.setProcessId(process);
//        return transaction;
//    }
//
//    private void executeTransaction(List<Transaction> raws) {
//        if (raws.isEmpty()) {
//            return;
//        } else {
////            removeAll(raws, raws.get(0).getTransactionDetail().getItem().getId());
//        }
//        executeTransaction(raws);
//    }
//
////    private void removeAll(List<Transaction> raws, String id) {
////        List<Transaction> actual = raws.stream()
////                .filter(b -> Objects.equals(b.getTransactionDetail().getItem().getId(), id))
////                .collect(Collectors.toList());
////        HttpEntity<List<Transaction>> request = new HttpEntity(actual);
////        restTemplate.postForObject(URL_MOVE, request, List.class);
////        System.out.println(actual.get(0).getTransactionDetail().getItem().getId());
////        raws.removeIf(b -> Objects.equals(b.getTransactionDetail().getItem().getId(), id));
////    }
//
//    private Transaction executeReplacement(Row row, int process) {
//        Transaction transaction = new Transaction();
////        ReplacementDetail detail = new ReplacementDetail();
//        Item item = new Item();
//        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
//            Cell cell = row.getCell(j);
//            try {
//                switch (j) {
//                    case 0:
//                        if (cell.getStringCellValue().equals("E")) {
//                            transaction.setType(TransactionType.ENTRY);
//                        } else if (cell.getStringCellValue().equals("S")) {
//                            transaction.setType(TransactionType.EGRESS);
//                        }
//                        break;
//                    case 1:
////                        detail.setTypeReplacement((int) cell.getNumericCellValue());
//                        break;
//                    case 2:
////                        detail.setNro(cell.getStringCellValue());
//                        break;
//                    case 3:
//                        item.setId(cell.getStringCellValue());
//
//                        break;
//                    case 5:
////                        detail.setSeccionReplacement(cell.getStringCellValue());
//                        break;
//                    case 7:
//                        transaction.setQuantity(new BigDecimal(cell.getNumericCellValue()));
//                        break;
//                    case 8:
//                        transaction.setPriceNeto(new BigDecimal(cell.getNumericCellValue()));
//
//                        break;
//                    case 9:
//                        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(cell.getStringCellValue());
//                        transaction.setTransactionDate(date1
//                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//                        break;
//                    case 10:
////                        detail.setNroAccountReplacement(cell.getStringCellValue());
//                        break;
//                }
//            } catch (IllegalStateException ex) {
//                System.out.println(ex);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
////        detail.setItem(item);
////        transaction.setTransactionDetail(detail);
//////        transaction.setProcessId(process);
////        System.out.println(transaction.getTransactionDetail().getItem().getId());
//        return transaction;
//    }
//
////    @Async("threadPoolTaskExecutor")
////    public CompletableFuture<Void> readFile(XSSFSheet sheet, Process processActive) {
////
////        switch (processActive.getTransactionOption()) {
////            case SALDO_INITIAL_MP:
////            case SALDO_INITIAL_R:
////                executeFileRead(sheet, processActive);
////                break;
////        }
////        return null;
////    }
//
////    private void executeFileRead(XSSFSheet sheet, Process process) {
////        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
////            HttpEntity<Transaction> request = new HttpEntity(executeInitial(sheet.getRow(i), process));
////            try {
//////                restTemplate.postForObject(URL, request, Transaction.class);
////            } catch (RestClientException exRestTemplate) {
////                System.out.println("failed to execute" + exRestTemplate.getMessage());
////            }
////
////        }
////    }
//
    public Process saveProcess(String option, String number) {
        Process process = new Process();
        process.setNumberProcess(Integer.valueOf(number));
        process.setTransactionOption(TransactionOption.valueOf(option));
        return processService.createProcess(process);
    }
}
