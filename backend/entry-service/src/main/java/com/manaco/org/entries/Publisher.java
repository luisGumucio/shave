package com.manaco.org.entries;

import com.manaco.org.model.Transaction;
import com.manaco.org.model.TransactionOption;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Publisher {


    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sentToTransaction(Transaction transaction, TransactionOption transactionOption) {
        String exchange = String.format("%s.direct", transactionOption.toString().toLowerCase());
        String routingKey = String.format("%s.routingkey", transactionOption.toString().toLowerCase());
        amqpTemplate.convertAndSend(exchange, routingKey, transaction);
    }

    public void sentToTransaction(List<Transaction> transaction) {
        String exchange = String.format("%s.direct", "producto1");
        String routingKey = String.format("%s.routingkey", "producto1");
        amqpTemplate.convertAndSend(exchange, routingKey, transaction);
    }
}
