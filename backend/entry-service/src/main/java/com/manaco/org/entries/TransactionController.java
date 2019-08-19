package com.manaco.org.entries;

import com.manaco.org.dto.FilterDate;
import com.manaco.org.dto.TransactionDto;
import com.manaco.org.model.Transaction;
import com.manaco.org.model.TransactionDetail;
import com.manaco.org.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;


//    @GetMapping
//    public Page<Transaction> get(@RequestParam(defaultValue = "0") int page, @RequestParam String identifier) {
//        return transactionDetail.findByIdentifier(new PageRequest(page, 10), identifier);
//    }

    @GetMapping
    public List<TransactionDto> get(@RequestParam(defaultValue = "0") int page, @RequestParam String identifier) {
        return transactionService.getTransactionDto(page, identifier);
    }

    @GetMapping(path = "/{id}")
    public Page<Transaction> get(@PathVariable String id, @RequestParam(defaultValue = "0") int page) {
        return transactionRepository.findByItemId(PageRequest.of(page, 20), id);
    }

    @GetMapping(path = "/identifier")
    public Page<Transaction> getType(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "EGRESS") String type,
                                     @RequestParam(defaultValue = "REPUESTOS") String identifier) {
        return transactionRepository.findByTypeAndIdentifier(PageRequest.of(page, 20), type, identifier);
    }

    @PostMapping(path = "/reportTransaction")
    public Page<Transaction> getType1(@RequestParam(defaultValue = "0") int page,
                                      @RequestBody FilterDate filterDate,
                                     @RequestParam(defaultValue = "0") String id) {

        if(filterDate.getLastDate() == null) {
            return transactionRepository.findByItemIdAndTransactionDate(PageRequest.of(page, 20),
                    id, filterDate.getInitDate());
        }
        return null;
    }


}
