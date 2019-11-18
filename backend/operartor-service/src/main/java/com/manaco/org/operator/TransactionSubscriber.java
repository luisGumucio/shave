package com.manaco.org.operator;

import com.manaco.org.model.Item;
import com.manaco.org.model.Transaction;
import com.manaco.org.model.Ufv;
import com.manaco.org.repositories.ItemRepository;
import com.manaco.org.repositories.UfvRepository;
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

    @Autowired
    private TransactionRepuestosService transactionRepuestosService;

    @Autowired
    private TransactionPTService transactionPTService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UfvRepository ufvRepository;

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
                Ufv actual = ufvRepository.findByCreationDate(transaction.getTransactionDate());
                Item item = itemRepository.findById(transaction.getItem().getId()).orElse(null);
                service.updateItem(item, transaction, actual);
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
                transactionPTService.executeMoving(transaction);
                break;
        }
    }
}
