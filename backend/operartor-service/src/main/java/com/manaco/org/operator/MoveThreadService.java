/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manaco.org.operator;

import java.util.*;

import com.manaco.org.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lucho
 */
@Service
public class MoveThreadService implements Runnable {

    private static final int TIME_TO_SLEEP = 2000;
    private final Queue<List<Transaction>> queue;
    @Autowired
    private TransactionService transactionService;

    public MoveThreadService() {
        queue = new ArrayDeque<>();
    }

    @Override
    public void run() {
        try {
            while (Boolean.TRUE) {
                if (queue.isEmpty()) {
                    Thread.sleep(TIME_TO_SLEEP);
                } else {
                    List<Transaction> next = queue.poll();
                    executeMoving(next);
                }
            }

        } catch (InterruptedException ex) {
//            LOGGER.error("Interrupted process of notification with error {}", ex);
            Thread.currentThread().interrupt();
        }
    }

    public void add(List<Transaction> transactions) {
        queue.add(transactions);
    }

    private synchronized void executeMoving(List<Transaction> next) {
        Collections.sort(next, (Transaction o1, Transaction o2) -> o1.getTransactionDate().compareTo(o2.getTransactionDate()));
        next.forEach((transaction) -> {
//            transactionService.saveMoving(transaction);
        });
    }
}
