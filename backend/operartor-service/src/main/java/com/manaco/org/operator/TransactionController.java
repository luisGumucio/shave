package com.manaco.org.operator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class TransactionController {

    @Autowired
    private TransactionPTService transactionPTService;


    @PostMapping
    public void execute() {
        transactionPTService.executeTransaction();
    }
}
