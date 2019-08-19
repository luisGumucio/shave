package com.manaco.org.operator;

import com.manaco.org.model.Item;
import com.manaco.org.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Queue;

@Service
public class TransactionThreadService implements Runnable {

    private static final int TIME_TO_SLEEP = 2000;
    private final Queue<Object> queue;

    @Autowired
    private TransactionService transactionService;

    public TransactionThreadService() {
        queue = new ArrayDeque<>();
    }

    public void add(Object transaction) {
        queue.add(transaction);
    }

    @Override
    public void run() {
        try {
            while (Boolean.TRUE) {
                if (queue.isEmpty()) {
                    Thread.sleep(TIME_TO_SLEEP);
                } else {
                    Object next = queue.poll();
                    executeInitial(next);
                }
            }

        } catch (InterruptedException ex) {
//            LOGGER.error("Interrupted process of notification with error {}", ex);
            Thread.currentThread().interrupt();
        }
    }

    private void executeInitial(Object next) {
        if (next instanceof Transaction) {
//            transactionService.saveItem((Transaction) next);
        } else if (next instanceof Item) {
//            transactionService.executeItem((Item) next);
        }
    }


//    @Transactional(rollbackFor = Exception.class)
//    private void execute(Raw raw) {
//        if (raw instanceof RawInitial) {
//            transactionService.saveItem((RawInitial) raw);
//        } else if (raw instanceof RawMaterial) {
//            transactionService.saveTransaction((RawMaterial) raw);
//        }
//    }
}
