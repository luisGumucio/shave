package com.manaco.org.operator;

import com.manaco.org.model.Transaction;
import com.manaco.org.repositories.*;
import com.manaco.org.utils.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionRepuestosService {


    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRepuestosService.class);

    @Autowired
    private UfvRepository ufvRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ProccessRepository proccessRepository;
    @Autowired
    private Operator operator;
    @Autowired
    private TransactionDetailRepository detailRepository;
    @Autowired
    private StockRepository stockRepository;

    public void saveItem(Transaction transaction) {
        if (transaction.getItem().getQuantity().intValue() < 0) {
            transaction.getItem().setIsFailure(Boolean.TRUE);
        } else {
            transaction.getItem().setIsFailure(Boolean.FALSE);
        }

        itemRepository.save(transaction.getItem());
        transactionRepository.save(transaction);
        LOGGER.info("adding initial transaction with item id" + transaction.getItem().getId());
    }
}
