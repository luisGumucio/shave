package com.manaco.org.entries.processator;

import com.manaco.org.entries.Publisher;
import com.manaco.org.model.*;
import com.manaco.org.model.Process;
import com.manaco.org.utils.ProcesatorObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class ProcesatorMoving implements ProcesatorObject {

    @Autowired
    private Publisher publisher;

    @Override
    public void execute(Map<String, String> map, TransactionOption option, Process processActive) {
        Transaction transaction = new Transaction();
        Item item = new Item();
        item.setId(map.get("ITEM"));
        item.setPrice(new BigDecimal(map.get("PU_ACTUAL").replace(",", ""))
                .setScale(6, BigDecimal.ROUND_DOWN));
        item.setQuantity(new BigDecimal(map.get("CANTIDAD").replace(",", "")));
        item.setIdentifier(option);


        if (map.get("TIPO").equals("E")) {
            transaction.setType(TransactionType.ENTRY);
        } else if (map.get("TIPO").equals("S")) {
            transaction.setType(TransactionType.EGRESS);
        }

        LocalDate currentDate = convertToDate(map.get("FECHA")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        transaction.setPriceActual(item.getPrice());
        transaction.setPriceNeto(BigDecimal.ZERO);
        transaction.setQuantity(item.getQuantity());
        transaction.setTransactionDate(currentDate);
        transaction.setPriceNeto(BigDecimal.ZERO);
        transaction.setUfvValue(BigDecimal.ZERO);
        transaction.setItem(item);
        transaction.setProcessId(processActive.getId());
        transaction.setDetail(buildDetail(map, option));
        publisher.sentToTransaction(transaction, option);
    }

    private TransactionDetail buildDetail(Map<String, String> map, TransactionOption option) {
        switch (option) {
            case PRIMA:
                return buildPrimaDetail(map);
//            case REPUESTOS:
//                initialExecute(file, processatorInitial, TransactionOption.REPUESTOS, processActive);
//                break;
//            case PRODUCTO:
//                return buildProductDetail(map);
        }
        return null;
    }
    private Date convertToDate(String receivedDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(receivedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private TransactionDetail buildPrimaDetail(Map<String, String> map) {
        TransactionDetail detail = new TransactionDetail();
        Map<String, String> info = new HashMap<>();
        info.put("CUENTA", map.get("CUENTA"));
        info.put("ALMACEN", map.get("ALMACEN"));
        info.put("SECCION_D", map.get("SECCION_D"));
        info.put("DESCRIPCION", map.get("DESCRIPCION"));
        detail.setInformation(info);
        return detail;
    }

//    private void executeMaterial(XSSFSheet sheet, int process, int numberProcess) {
//        if (numberProcess == 1) {
//            List<Transaction> raws = new ArrayList<>();
//            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
//                raws.add(executeMaterial(sheet.getRow(i), process));
//            }
//
//            Collections.sort(raws);
//            System.out.println(raws.get(0).getDate());
//            executeTransaction(raws);
//        } else if (numberProcess == 2) {
//            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
//                executeSecondProcess(sheet.getRow(i));
//            }
//        }
//    }
}
