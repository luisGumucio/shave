package com.manaco.org.operator;

import com.manaco.org.common.model.Item;
import com.manaco.org.common.model.Transaction;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionThreadService threadService;
    @Autowired
    private MoveThreadService moveThreadService;

    @PostConstruct
    private void init() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(threadService);
        ExecutorService moveService = Executors.newSingleThreadExecutor();
        moveService.execute(moveThreadService);
    }

    @PostMapping
    public Transaction postRaw(@RequestBody Transaction raw) {
        threadService.add(raw);
        return raw;
    }
    
    @PostMapping("/material")
    public List<Transaction> postTransactions(@RequestBody List<Transaction> transactions) {
        moveThreadService.add(transactions);
        return transactions;
    }

    @PostMapping("/item")
    public Item postItem(@RequestBody Item item) {
        threadService.add(item);
        return item;
    }
}
