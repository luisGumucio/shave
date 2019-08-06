package com.manaco.org.entries.processator;

import com.manaco.org.entries.Publisher;
import com.manaco.org.model.*;
import com.manaco.org.model.Process;
import com.manaco.org.utils.ProcesatorObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

public class ProcesatorMoving implements ProcesatorObject {

    @Autowired
    private Publisher publisher;

    @Override
    public void execute(Map<String, String> map, TransactionOption option, Process processActive) {
        Transaction transaction = new Transaction();
        Item item = new Item();

        transaction.setType(TransactionType.INITIAL);


        item.setId(map.get("ITEM"));
        item.setPrice(new BigDecimal(map.get("PU_ACTUAL").replace(",", ""))
                .setScale(6, BigDecimal.ROUND_DOWN));
        item.setQuantity(new BigDecimal(map.get("F_SALDO").replace(",", "")));
        item.setInitialDate(new Date(map.get("FECHA")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        item.setLastUpdate(item.getInitialDate());


        transaction.setPriceActual(item.getPrice());
        transaction.setPriceNeto(BigDecimal.ZERO);
        transaction.setQuantity(item.getQuantity());
        transaction.setTransactionDate(item.getInitialDate());
        transaction.setPriceNeto(BigDecimal.ZERO);
        transaction.setUfvValue(BigDecimal.ZERO);
        transaction.setItem(item);
        transaction.setItemId(item.getId());
        transaction.setProcessId(processActive.getId());

        publisher.sentToTransaction(transaction, option);
    }
}
