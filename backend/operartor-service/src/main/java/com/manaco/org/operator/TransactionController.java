package com.manaco.org.operator;

import com.manaco.org.model.Item;

import java.time.LocalDate;
import java.util.List;

import com.manaco.org.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionThreadService threadService;
    @Autowired
    private MoveThreadService moveThreadService;
    @Autowired
    private TransactionService transactionService;

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

    @RequestMapping(method = RequestMethod.PUT)
    public void updateItem(@RequestBody String date) {
        LocalDate localDate = LocalDate.parse(date);
//        transactionService.updateItem(localDate);
    }
}
