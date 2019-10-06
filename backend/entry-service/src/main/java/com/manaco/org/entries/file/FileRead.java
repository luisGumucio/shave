package com.manaco.org.entries.file;

import com.manaco.org.model.*;
import com.manaco.org.model.Process;
import org.springframework.http.HttpEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class FileRead {


    public static void main(String args[]) {
        Transaction transaction = new Transaction();
        List<String> fileAdd = new ArrayList<>();

        List<Transaction> move = new ArrayList<>();
        long start = new Date().getTime();
        System.out.println("BufferedReader Time Consumed => " + new Date().toString());
        String fileName = "C:\\Users\\lucho\\Documents\\manaco\\datosOficial\\productoTerminado\\move.txt"; //this path is on my local
        int cont = 0;
        try (BufferedReader fileBufferReader = new BufferedReader(new FileReader(fileName))) {
            String fileLineContent;
            while ((fileLineContent = fileBufferReader.readLine()) != null) {
                if (cont == 0) {
                    cont++;
                } else {
                    System.out.println(fileLineContent);
                    cont++;
//                    move.add(readContent(fileLineContent));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

//        for (String value: fileAdd) {
//            move.add(readContent(value));
//        }
        long end = new Date().getTime();

        long time = (long) (end - start);
        executeTransaction(move);
        System.out.println("BufferedReader Time Consumed => " + time);
        System.out.println("total Time Consumed => " + move.size());
        System.out.println(cont);
    }

    private static Transaction readContent(String content) {
        try {
            String value = "";
            int cont = 0;
            TransactionDetail detail = new TransactionDetail();
            Map<String, String> info = new HashMap<>();
            Transaction transaction = new Transaction();
            Item item = new Item();
            for (int x = 0; x < content.length(); x++) {
                if (String.valueOf(content.charAt(x)).equals("|")) {
                    switch (cont) {
                        case 0:
                            if (value.equals("E")) {
                                transaction.setType(TransactionType.ENTRY);
                            } else if (value.equals("S")) {
                                transaction.setType(TransactionType.EGRESS);
                            }
                            break;
                        case 1:
                            info.put("ALMACEN", value);
                            break;
                        case 2:
                            info.put("NRO_DOC", value);
                            break;
                        case 3:
                            item.setId(value);
                            break;
                        case 4:
                            item.setQuantity(new BigDecimal(value));
                            break;
                        case 5:
                            item.setPrice(new BigDecimal(value));
                            break;
                        case 6:
                            LocalDate currentDate = convertToDate(value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            transaction.setTransactionDate(currentDate);
                            break;
                        case 7:
                            info.put("ORIGEN", value);
                            break;
                        case 8:
                            info.put("SEMANA", value);
                            break;
                        case 10:
                            info.put("TIPO_MOV", value);
                            break;
                    }
                    cont++;
                    value = "";
                } else {
                    value = value.concat(String.valueOf(content.charAt(x)));
                }
            }
            detail.setInformation(info);
            item.setIdentifier(TransactionOption.PRODUCTO);
            transaction.setItem(item);
            transaction.setDetail(detail);
            if (transaction.getType() == TransactionType.ENTRY) {
                transaction.setPriceActual(item.getPrice());
                transaction.setPriceNeto(item.getPrice());
            } else if (transaction.getType() == TransactionType.EGRESS) {
                transaction.setPriceActual(item.getPrice());
                transaction.setPriceNeto(BigDecimal.ZERO);
            }
            transaction.setIdentifier(item.getIdentifier());
            return transaction;
        } catch (Exception exception) {
            System.out.println(exception);
            return null;
        }

    }

    private static Date convertToDate(String receivedDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(receivedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static void executeTransaction(List<Transaction> raws) {
        if (raws.isEmpty()) {
            return;
        } else {
            removeAll(raws, raws.get(0).getItem().getId());
        }
        executeTransaction(raws);
    }

    private static void removeAll(List<Transaction> raws, String id) {
        try {
            List<Transaction> actual = raws.stream()
                    .filter(b -> Objects.equals(b.getItem().getId(), id))
                    .collect(Collectors.toList());
            Collections.sort(actual);
            System.out.println(actual.get(0).getItem().getId());
            raws.removeIf(b -> Objects.equals(b.getItem().getId(), id));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


