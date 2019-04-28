package com.manaco.org.entries;

import com.manaco.org.dto.TransactionDetailDto;
import com.manaco.org.model.Transaction;
import com.manaco.org.repositories.TransactionDetailRepository;
import com.manaco.org.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionDetailRepository transactionDetailRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Page<Transaction> findById(@PathVariable long id, @RequestParam(defaultValue = "0") int page) {
        return transactionRepository.findByTransactionDetailItemItemIdOrderByDateAsc(new PageRequest(page, 4), id);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public List<TransactionDetailDto> find() {
//        return transactionDetailRepository.fetchTransactionDetailDtoJoin();
//    }
}
