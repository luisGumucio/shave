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
        switch (transaction.getType()) {
            case INITIAL:
                service.saveItem(transaction);
                break;
        }
    }

    @RabbitListener(queues = "${prima.rabbitmq.queue}")
    public synchronized void receivedPrima(Transaction transaction) {
        switch (transaction.getType()) {
            case INITIAL:
                service.saveItem(transaction);
                break;
            default:
                service.executeMoving(transaction);
                break;
        }
        LOGGER.info("recibido");
    }

    @RabbitListener(queues = "${producto.rabbitmq.queue}")
    public synchronized void receivedProducto(Transaction transaction) {
        switch (transaction.getType()) {
            case INITIAL:
                service.saveItemProduct(transaction);
                break;
        }
    }
}
