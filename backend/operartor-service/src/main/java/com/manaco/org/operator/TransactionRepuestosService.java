package com.manaco.org.operator;

import com.manaco.org.model.Item;
import com.manaco.org.model.Process;
import com.manaco.org.model.Transaction;
import com.manaco.org.model.TransactionType;
import com.manaco.org.model.Ufv;
import com.manaco.org.repositories.*;
import com.manaco.org.utils.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    public void updateItem(Transaction transaction) {
        Process process = proccessRepository.findByIsActive(true);

        Ufv actual = ufvRepository.findByCreationDate(transaction.getTransactionDate());
        Ufv before = ufvRepository.findByCreationDate(transaction.getItem().getLastUpdate());
        if (transaction.getItem().getQuantity().intValue() != 0) {
            BigDecimal totalNormal = operator.calculateTotal(transaction.getItem().getQuantity(), transaction.getItem().getPrice());

            BigDecimal totalUpdate = operator.calculateUpdate(totalNormal, actual.getValue(), before.getValue());
            BigDecimal newPrice = operator.newPrice(totalUpdate, transaction.getItem().getQuantity());
            BigDecimal increment = operator.caclulateUfvValue(totalUpdate, totalNormal);
            transaction.getItem().setPrice(newPrice.setScale(6, BigDecimal.ROUND_CEILING));
            transaction.getItem().setLastUpdate(transaction.getTransactionDate());
            transaction.setUfv(actual);
            transaction.setIncrement(increment);

            transaction.setProcessId(process.getId());
            transaction.getItem().setPrice(newPrice.setScale(6, BigDecimal.ROUND_CEILING));
            transaction.getItem().setLastUpdate(transaction.getTransactionDate());
            saveMove(transaction.getItem(), TransactionType.CIERRE, increment, totalNormal, totalUpdate,
                    actual, transaction.getProcessId());
        } else {
            transaction.getItem().setLastUpdate(transaction.getTransactionDate());
            saveMove(transaction.getItem(), TransactionType.CIERRE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    actual, transaction.getProcessId());
        }
        LOGGER.info("Updating transaction with item id" + transaction.getItem().getId());
    }

    private void saveMove(Item item, TransactionType type, BigDecimal increment, BigDecimal totalNormal,
                          BigDecimal totalUpdate, Ufv actual,
                          String proccesID) {
        Transaction transaction = new Transaction();
        transaction.setItem(item);
        transaction.setType(type);
        transaction.setTransactionDate(item.getLastUpdate());
        transaction.setBalance(item.getQuantity());
        transaction.setPriceActual(item.getPrice());
        transaction.setUfv(actual);
        transaction.setTotalNormal(totalNormal);
        transaction.setTotalUpdate(totalUpdate);
        transaction.setIncrement(increment);
        transaction.setProcessId(proccesID);
        transaction.setIdentifier(item.getIdentifier());
        itemRepository.save(item);
        transactionRepository.save(transaction);
    }
}
