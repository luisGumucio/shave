package com.manaco.org.entries;

import com.manaco.org.model.Transaction;
import com.manaco.org.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;


    @GetMapping
    public Page<Transaction> get(@RequestParam(defaultValue = "0") int page, @RequestParam String identifier) {
        return transactionRepository.findByIdentifier(new PageRequest(page, 10), identifier);
    }
}
