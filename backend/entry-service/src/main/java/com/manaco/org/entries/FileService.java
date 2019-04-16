package com.manaco.org.entries;

import com.manaco.org.common.model.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class FileService {

    private static final String URL = "http://localhost:4001/transaction";
    private static final String URL_MOVE = "http://localhost:4001/transaction/material";
    private RestTemplate restTemplate;

    public FileService() {
        restTemplate = new RestTemplate();
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Void> readFile(XSSFSheet sheet, TransactionOption option, int process,
            int numberProcess) {
        switch (option) {
            case SALDO_INITIAL_MP:
                executeFileRead(sheet, process);
                break;
            case MATERIA_PRIMA:
                executeMaterial(sheet, process, numberProcess);
        }
        return null;
    }

    private void executeMaterial(XSSFSheet sheet, int process, int numberProcess) {
        if (numberProcess == 1) {
            List<Transaction> raws = new ArrayList<>();
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                raws.add(executeMaterial(sheet.getRow(i), process));
            }

            Collections.sort(raws);
            System.out.println(raws.get(0).getDate());
            executeTransaction(raws);
        } else if (numberProcess == 2) {
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                 executeSecondProcess(sheet.getRow(i));
            }
        }

    }

    private void executeSecondProcess(Row row) {
        Item item = new Item();
        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            try {
                switch (j) {
                    case 0:
                        item.setId(Long.parseLong(cell.getStringCellValue()));
                        break;
                    case 1:
                        item.setPrice(new BigDecimal(cell.getNumericCellValue()));
                        break;
                }

            } catch (IllegalStateException ex) {
                System.out.print(ex);
            }
        }
        HttpEntity<Item> request = new HttpEntity(item);
        restTemplate.postForObject(URL + "/item", request, Item.class);
        System.out.println(item.getId());
    }

    private void executeFileRead(XSSFSheet sheet, int process) {
        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
            HttpEntity<Transaction> request = new HttpEntity(executeInitial(sheet.getRow(i), process));
            try {
                restTemplate.postForObject(URL, request, Transaction.class);
            } catch (RestClientException exRestTemplate) {
                System.out.println("failed to execute" + exRestTemplate.getMessage());
            }

        }
    }

    private Transaction executeInitial(Row row, int process) {
        Transaction transaction = new Transaction();
        MateriaDetail detail = new MateriaDetail();
        Item item = new Item();
        Ufv ufv = new Ufv();
        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            try {
                switch (j) {
                    case 1:
                        item.setId(Long.parseLong(cell.getStringCellValue()));
                        break;
                    case 3:
                        item.setInitialDate(cell.getDateCellValue()
                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        item.setLastUpdate(cell.getDateCellValue()
                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        transaction.setDate(item.getLastUpdate());
                        break;
                    case 6:
                        item.setQuantity(new BigDecimal(cell.getNumericCellValue()));
                        transaction.setQuantity(item.getQuantity());
                        break;
                    case 8:
                        item.setPrice(new BigDecimal(cell.getNumericCellValue()).setScale(6, BigDecimal.ROUND_CEILING));
                        transaction.setPriceActual(item.getPrice());
                        transaction.setPriceNeto(BigDecimal.ZERO);
                        break;
                    case 9:
                        BigDecimal decimal = new BigDecimal(cell.getNumericCellValue());
                        ufv.setValue(decimal.setScale(5, BigDecimal.ROUND_DOWN));
                        ufv.setCreationDate(item.getLastUpdate());
                        break;
                }

            } catch (IllegalStateException ex) {
                System.out.print(ex);
            }
        }
        detail.setUfv(ufv);
        detail.setItem(item);
        transaction.setType(TransactionType.INITIAL);
        transaction.setUfvValue(BigDecimal.ZERO);
        transaction.setTransactionDetail(detail);
        transaction.setProcessId(process);
        return transaction;
    }

    private Transaction executeMaterial(Row row, int process) {
        Transaction transaction = new Transaction();
        MateriaDetail detail = new MateriaDetail();
        Item item = new Item();
        for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            try {
                switch (j) {
                    case 0:
                        transaction.setDate(cell.getDateCellValue()
                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        break;
                    case 1:
                        item.setId(Integer.parseInt(cell.getStringCellValue()));
                        break;
                    case 2:
                        if (cell.getStringCellValue().equals("E")) {
                            transaction.setType(TransactionType.ENTRY);
                        } else if (cell.getStringCellValue().equals("S")) {
                            transaction.setType(TransactionType.EGRESS);

                        }
                        break;
                    case 3:
                        transaction.setQuantity(new BigDecimal(cell.getNumericCellValue()));
                        break;
                    case 5:
                        transaction.setPriceNeto(new BigDecimal(cell.getNumericCellValue()));
                        break;
                    case 7:
                        detail.setSeccion(Integer.parseInt(cell.getStringCellValue()));
                        break;
                    case 8:
                        detail.setNroAccount(cell.getStringCellValue());
                        break;
                    case 9:
                        detail.setDescription(cell.getStringCellValue());
                        break;
                    case 10:
                        detail.setWareHouseId(Integer.parseInt(cell.getStringCellValue()));
                        break;
                }
            } catch (IllegalStateException ex) {
                System.out.print(ex);
            }
        }
        detail.setItem(item);
        transaction.setTransactionDetail(detail);
        transaction.setProcessId(process);
        return transaction;
    }

    private void executeTransaction(List<Transaction> raws) {
        if (raws.isEmpty()) {
            return;
        } else {
            removeAll(raws, raws.get(0).getTransactionDetail().getItem().getId());
        }
        executeTransaction(raws);
    }

    private void removeAll(List<Transaction> raws, Long id) {
        List<Transaction> actual = raws.stream()
                .filter(b -> Objects.equals(b.getTransactionDetail().getItem().getId(), id))
                .collect(Collectors.toList());
        HttpEntity<List<Transaction>> request = new HttpEntity(actual);
        restTemplate.postForObject(URL_MOVE, request, List.class);
        System.out.println(actual.get(0).getTransactionDetail().getItem().getId());
        raws.removeIf(b -> Objects.equals(b.getTransactionDetail().getItem().getId(), id));
    }
}
