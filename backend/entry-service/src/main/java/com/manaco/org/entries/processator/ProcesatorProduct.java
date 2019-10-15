package com.manaco.org.entries.processator;

import com.manaco.org.entries.Publisher;
import com.manaco.org.model.*;
import com.manaco.org.model.Process;
import com.manaco.org.utils.ProcesatorObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProcesatorProduct implements ProcesatorObject {

    @Autowired
    private Publisher publisher;

    @Autowired
    private Data data;

    private int channel;

    private List<Transaction> transactions;

    public ProcesatorProduct() {

        transactions = new ArrayList<>();
    }

    public void load() {
            data.load();
            channel = 1;
    }

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
            ItemTemp temp = data.filter(item.getId());
            if(temp != null) {
                item.setPrice(temp.getCosto().setScale(6, BigDecimal.ROUND_DOWN));
            }

            transaction.setPriceActual(item.getPrice());
            transaction.setPriceNeto(item.getPrice());
            transaction.setDetail(buildDetail(map, option));
        } else if (map.get("TIPO").equals("S")) {
            transaction.setType(TransactionType.EGRESS);
            transaction.setPriceActual(item.getPrice());
            transaction.setPriceNeto(BigDecimal.ZERO);
            transaction.setDetail(buildDetail(map, option));
        } else if (map.get("TIPO").equals("EC")) {
            ItemTemp temp = data.filter(item.getId());
            if(temp != null) {
                item.setPrice(temp.getCosto().setScale(6, BigDecimal.ROUND_DOWN));
            }
            transaction.setType(TransactionType.ENTRY_BUY);
            transaction.setPriceActual(item.getPrice());
            transaction.setPriceNeto(item.getPrice());
            transaction.setDetail(buildDetail(map, option));
        } else if (map.get("TIPO").equals("CAM")) {
            transaction.setType(TransactionType.CAM);
            transaction.setDetail(buildDetail(map, option));
        }

        LocalDate currentDate = convertToDate(map.get("FECHA")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        transaction.setTransactionDate(currentDate);
        transaction.setItem(item);
        transaction.setProcessId(processActive.getId());
        transaction.setDetail(buildDetail(map, option));
        transactions.add(transaction);
    }

    @Override
    public void execute(InputStream file, TransactionOption option, Process processActive) {
        List<String> fileAdd = new ArrayList<>();
        int cont = 0;
        try (BufferedReader fileBufferReader = new BufferedReader(new InputStreamReader(
                file, StandardCharsets.UTF_8))) {
            String fileLineContent;
            while ((fileLineContent = fileBufferReader.readLine()) != null) {
                if (cont == 0) {
                    cont++;
                } else {
                    System.out.println(fileLineContent);
                    cont++;
//                    fileAdd.add(fileLineContent);
                    publisher.sentToTransaction(readContent(fileLineContent, processActive), option);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  Transaction readContent(String content, Process process) {
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
                            } else if (value.equals("EC")) {
                                transaction.setType(TransactionType.ENTRY_BUY);
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
            if (transaction.getType() == TransactionType.ENTRY || transaction.getType() == TransactionType.ENTRY_BUY) {
                transaction.setPriceActual(item.getPrice());
                transaction.setPriceNeto(item.getPrice());
            } else if (transaction.getType() == TransactionType.EGRESS) {
                transaction.setPriceActual(item.getPrice());
                transaction.setPriceNeto(BigDecimal.ZERO);
            }
            transaction.setIdentifier(item.getIdentifier());
            transaction.setProcessId(process.getId());
            return transaction;
        } catch (Exception exception) {
            System.out.println(exception);
            return null;
        }
    }

    private  Date convertToDate(String receivedDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(receivedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private TransactionDetail buildDetail(Map<String, String> map, TransactionOption option) {
        switch (option) {
            case PRODUCTO:
                return buildProductDetail(map);
        }
        return null;
    }

    private TransactionDetail buildProductDetail(Map<String, String> map) {
        TransactionDetail detail = new TransactionDetail();
        Map<String, String> info = new HashMap<>();
        info.put("ALMACEN", map.get("ALMACEN"));
        info.put("NRO_DOC", map.get("NRO_DOC"));
        info.put("SEMANA", map.get("SEMANA"));
        info.put("TAB_ORIG", map.get("TAB_ORIG"));
        info.put("TIPO_MOV", map.get("TIPO_MOV"));
        detail.setInformation(info);
        return detail;
    }

    public void sendTransaction() {
        executeTransaction(transactions);
    }

    private void executeTransaction(List<Transaction> raws) {
        if (raws.isEmpty()) {
            return;
        } else {
            removeAll(raws, raws.get(0).getItem().getId());
        }
        executeTransaction(raws);
    }

    private synchronized void removeAll(List<Transaction> raws, String id) {
        try {
            List<Transaction> actual = raws.stream()
                    .filter(b -> Objects.equals(b.getItem().getId(), id))
                    .collect(Collectors.toList());
            if(channel == 1) {
                channel++;
            } else {
                channel--;
            }
//            System.out.println(channel);
//            publisher.sentToTransaction(actual, channel);
            System.out.println( "actual: " + actual.get(0).getItem().getId() + "current: " + id);
            publisher.sentToTransaction(actual, channel);
            raws.removeIf(b -> b.getItem().getId().equals(id));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
