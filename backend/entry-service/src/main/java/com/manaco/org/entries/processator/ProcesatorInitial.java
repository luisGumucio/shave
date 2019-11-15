package com.manaco.org.entries.processator;

import com.manaco.org.entries.Publisher;
import com.manaco.org.model.*;
import com.manaco.org.model.Process;
import com.manaco.org.repositories.UfvRepository;
import com.manaco.org.utils.ProcesatorObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProcesatorInitial implements ProcesatorObject {

    @Autowired
    private Publisher publisher;

    @Autowired
    private UfvRepository ufvRepository;

    @Override
    public void execute(Map<String, String> map, TransactionOption option, Process processActive) {
        Transaction transaction = new Transaction();
        Item item = new Item();
        item.setId(map.get("ITEM"));
        item.setPrice(new BigDecimal(map.get("PU_ACTUAL").replace(",", ""))
                .setScale(6, BigDecimal.ROUND_DOWN));
        item.setQuantity(new BigDecimal(map.get("F_SALDO").replace(",", "")));
        LocalDate currentDate = convertToDate(map.get("FECHA")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        item.setInitialDate(currentDate);
        item.setLastUpdate(item.getInitialDate());
        item.setIdentifier(option);
        item.setTotal(item.getPrice().multiply(item.getQuantity()));
        item.setTotalUpdate(item.getPrice().multiply(item.getQuantity()));

        transaction.setType(TransactionType.INITIAL);
        transaction.setPriceActual(item.getPrice());
        transaction.setPriceNeto(BigDecimal.ZERO);
        transaction.setBalance(item.getQuantity());
        transaction.setTransactionDate(item.getInitialDate());
        transaction.setItem(item);
        transaction.setTotalNormal(item.getTotal());
        transaction.setTotalUpdate(item.getTotal());
        transaction.setIncrement(BigDecimal.ZERO);
        transaction.setProcessId(processActive.getId());
        transaction.setUfv(ufvRepository.findByCreationDate(item.getLastUpdate()));
        transaction.setDetail(buildDetail(map, option));
        transaction.setIdentifier(option);
        publisher.sentToTransaction(transaction, option);
    }

    @Override
    public void execute(InputStream file, TransactionOption option, Process processActive) {

    }

    private TransactionDetail buildDetail(Map<String, String> map, TransactionOption option) {
        switch (option) {
//            case PRIMA:
//                return buildPrimaDetail(map);
//            case REPUESTOS:
//                initialExecute(file, processatorInitial, TransactionOption.REPUESTOS, processActive);
//                break;
            case PRODUCTO:
                return buildProductDetail(map);
        }
        return null;
    }

    private TransactionDetail buildProductDetail(Map<String, String> map) {
        TransactionDetail detail = new TransactionDetail();
        Map<String, String> info = new HashMap<>();
        info.put("TIENDA", map.get("TIENDA"));
        info.put("PK_SEMANA", map.get("PK_SEMANA"));
        detail.setInformation(info);
        return detail;
    }

    private TransactionDetail buildPrimaDetail(Map<String, String> map) {
        TransactionDetail detail = new TransactionDetail();
        Map<String, String> info = new HashMap<>();
        info.put("CUENTA", map.get("CUENTA"));
        info.put("ALMACEN", map.get("ALMACEN"));
        detail.setInformation(info);
        return detail;
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

}
