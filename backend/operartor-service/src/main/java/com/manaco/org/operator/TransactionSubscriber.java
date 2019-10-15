package com.manaco.org.operator;

import com.manaco.org.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TransactionSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionSubscriber.class);

    @Autowired
    private TransactionService service;

    @Autowired
    private TransactionRepuestosService transactionRepuestosService;

    @Autowired
    private TransactionPTService transactionPTService;

    @RabbitListener(queues = "${repuestos.rabbitmq.queue}")
    public synchronized void receiveInitial(Transaction transaction) {
        switch (transaction.getType()) {
            case INITIAL:
                transactionRepuestosService.saveItem(transaction);
                break;
            case UPDATE:
                transactionRepuestosService.updateItem(transaction);
            default:
                service.executeMoving(transaction);
                break;
        }
    }

    @RabbitListener(queues = "${prima.rabbitmq.queue}")
    public synchronized void receivedPrima(Transaction transaction) {
        switch (transaction.getType()) {
            case INITIAL:
                service.saveItem(transaction);
                break;
            case SECOND_PROCESSS:
                service.executeSecondProcess(transaction);
                break;
            case UPDATE:
                transactionRepuestosService.updateItem(transaction);
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
                transactionPTService.saveItemProduct(transaction);
                break;
            default:
//                transactionPTService.loadData(transaction);
//                transactionPTService.executeMoving(transaction);
                break;
        }
    }

    @RabbitListener(queues = "${other.rabbitmq.queue}")
    public synchronized void receivedProducto1(List<Transaction> transaction) {
//        System.out.println(transaction.get(0).getItem().getId());
        transaction.forEach(b -> {
            transactionPTService.executeMoving(b);
        });
    }
}
