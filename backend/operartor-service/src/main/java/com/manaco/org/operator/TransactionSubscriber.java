package com.manaco.org.operator;

import com.manaco.org.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TransactionSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionSubscriber.class);

    @Autowired
    private TransactionService service;


    @RabbitListener(queues="${repuestos.rabbitmq.queue}")
    public synchronized void receiveInitial(Transaction transaction) {
        LOGGER.info("adding new transaction" + new Date().getTime());
//        transactionRepository.save(transaction);
    }

    @RabbitListener(queues = "${prima.rabbitmq.queue}")
    public void receivedPrima(Transaction transaction) {
        switch (transaction.getType()) {
            case INITIAL:
                service.saveItem(transaction);
                break;
        }
    }
}
