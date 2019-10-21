package com.manaco.org.operator;

import com.manaco.org.model.*;

import com.manaco.org.repositories.*;
import com.manaco.org.utils.Operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.manaco.org.model.TransactionType.INITIAL;

@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

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
    @Autowired
    private ItemStockRepository itemStockRepository;

    public void executeMoving(Transaction transaction) {
        Item item = itemRepository.findById(transaction.getItem().getId()).orElse(null);
        if (item != null) {
            Ufv actual = ufvRepository.findByCreationDate(transaction.getTransactionDate());
            item = updateItem(item, transaction, actual);
            switch (transaction.getType()) {
                case ENTRY:
                    executeEntry(item, transaction, actual);
                    break;
                case EGRESS:
                    executeEgress(item, transaction, actual);
                    break;
            }
            LOGGER.info("saved successfully with id " + transaction.getItem());
        } else {
            System.out.println("item not found with  id " + transaction.getItem());
            saveItemNotFound(transaction);
        }

    }

    private void saveItemNotFound(Transaction otherTransaction) {
        Transaction transaction = new Transaction();
        Item item = new Item();
        item.setId(otherTransaction.getItem().getId());
        item.setPrice(otherTransaction.getItem().getPrice());
        item.setQuantity(BigDecimal.ZERO);
        LocalDate currentDate = otherTransaction.getTransactionDate();
        item.setInitialDate(currentDate);
        item.setLastUpdate(item.getInitialDate());
        item.setTotal(BigDecimal.ZERO);

        transaction.setType(INITIAL);
        transaction.setPriceActual(item.getPrice());
        transaction.setPriceNeto(BigDecimal.ZERO);
        transaction.setBalance(BigDecimal.ZERO);
        transaction.setTransactionDate(item.getInitialDate());
        transaction.setItem(item);
        transaction.setTotalNormal(BigDecimal.ZERO);
        transaction.setTotalUpdate(BigDecimal.ZERO);
        transaction.setIncrement(BigDecimal.ZERO);
        transaction.setProcessId(otherTransaction.getProcessId());
        transaction.setUfv(ufvRepository.findByCreationDate(item.getLastUpdate()));
        transaction.setInformation(null);
        transaction.setIdentifier(otherTransaction.getItem().getIdentifier());
//        saveItem(transaction);
        if (transaction.getItem().getQuantity().intValue() < 0) {
            transaction.getItem().setIsFailure(Boolean.TRUE);
        } else {
            transaction.getItem().setIsFailure(Boolean.FALSE);
        }
        transaction.setProcessId(otherTransaction.getProcessId());
        item.setIdentifier(transaction.getIdentifier());
        itemRepository.save(transaction.getItem());
        transactionRepository.save(transaction);
        LOGGER.info("adding initial transaction with item id: " + transaction.getItem().getId());
        executeMoving(otherTransaction);
    }

    private Item updateItem(Item item, Transaction transaction, Ufv actual) {
        if (!item.getLastUpdate().equals(transaction.getTransactionDate())) {
            Ufv before = ufvRepository.findByCreationDate(item.getLastUpdate());
            if (item.getQuantity().intValue() != 0) {
                BigDecimal totalNormal = operator.calculateTotal(item.getQuantity(), item.getPrice());
                BigDecimal totalUpdate = operator.calculateUpdate(totalNormal, actual.getValue(), before.getValue());
                BigDecimal newPrice = operator.newPrice(totalUpdate, item.getQuantity());
                BigDecimal increment = operator.caclulateUfvValue(totalUpdate, totalNormal);
                item.setPrice(newPrice.setScale(6, BigDecimal.ROUND_CEILING));
                item.setLastUpdate(transaction.getTransactionDate());
                saveMove(item, TransactionType.UPDATE, increment, totalNormal, totalUpdate,
                        actual, transaction.getProcessId());
            } else {
                item.setLastUpdate(transaction.getTransactionDate());
                saveMove(item, TransactionType.UPDATE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        actual, transaction.getProcessId());
            }
        }
        return item;
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

    private void executeEgress(Item item, Transaction transaction, Ufv actual) {
        Transaction egress = new Transaction();
        egress.setItem(item);
        egress.setType(TransactionType.EGRESS);
        egress.setTransactionDate(transaction.getTransactionDate());
        egress.setEgress(transaction.getItem().getQuantity());

        //calculando la salida del item
        egress.setBalance(item.getQuantity().subtract(transaction.getItem().getQuantity()));
        egress.setPriceActual(item.getPrice());
        egress.setTotalEgress(item.getPrice().multiply(transaction.getItem().getQuantity()));

        if (egress.getBalance().equals(BigDecimal.ZERO)) {
            egress.setTotalNormal(BigDecimal.ZERO);
            egress.setTotalUpdate(BigDecimal.ZERO);
            item.setTotal(BigDecimal.ZERO);
            item.setQuantity(BigDecimal.ZERO);
        } else {
            item.setTotal(item.getTotal().subtract(egress.getTotalEgress()));
            egress.setTotalNormal(item.getTotal());
            BigDecimal itemQuantityTotal = operator.calculateQuantityTotal(item.getQuantity(), item.getPrice());
            egress.setTotalUpdate(itemQuantityTotal.subtract(egress.getTotalEgress()));
            item.setQuantity(egress.getBalance());
        }

        egress.setUfv(actual);
        egress.setIncrement(BigDecimal.ZERO);
        egress.setProcessId(transaction.getProcessId());
        egress.setIdentifier(item.getIdentifier());
        egress.setInformation(transaction.getInformation());
        item.setLastUpdate(transaction.getTransactionDate());
        itemRepository.save(item);

        transactionRepository.save(egress);
    }

    private void executeEntry(Item item, Transaction transaction, Ufv actual) {
        Transaction entry = new Transaction();
        entry.setItem(item);
        entry.setType(TransactionType.ENTRY);
        entry.setTransactionDate(transaction.getTransactionDate());
        entry.setEntry(transaction.getItem().getQuantity());
        //calculando la entrada del item
        entry.setBalance(item.getQuantity().add(transaction.getItem().getQuantity()));
        entry.setPriceNeto(transaction.getPriceActual());
        entry.setPriceActual(item.getPrice());
        entry.setTotalEntry(transaction.getItem().getPrice().multiply(transaction.getItem().getQuantity()));
        item.setTotal(item.getTotal().add(entry.getTotalEntry()));
        entry.setTotalNormal(item.getTotal());
        BigDecimal itemQuantityTotal = operator.calculateQuantityTotal(item.getQuantity(), item.getPrice());
        entry.setTotalUpdate(itemQuantityTotal.add(entry.getTotalEntry()));
        item.setQuantity(entry.getBalance());
        entry.setUfv(actual);
        entry.setIncrement(BigDecimal.ZERO);
        entry.setProcessId(transaction.getProcessId());
        entry.setIdentifier(item.getIdentifier());
        entry.setInformation(transaction.getInformation());
        item.setPrice(entry.getPriceActual());
        item.setLastUpdate(transaction.getTransactionDate());
        itemRepository.save(item);
        transactionRepository.save(entry);
    }

    public void saveItem(Transaction transaction) {

        if (transaction.getItem().getQuantity().intValue() < 0) {
            transaction.getItem().setIsFailure(Boolean.TRUE);
        } else {
            transaction.getItem().setIsFailure(Boolean.FALSE);
        }

        itemRepository.save(transaction.getItem());
//        if (transaction.getInformation() != null) {
//            detailRepository.save(transaction.getTransactionDetail());
//        } else {
//
//        }

        transactionRepository.save(transaction);
        LOGGER.info("adding initial transaction with item id" + transaction.getItem().getId());
    }


    public void executeSecondProcess(Transaction current) {
        Item item = itemRepository.findById(current.getItem().getId()).orElse(null);
        List<Transaction> transactions = transactionRepository.findByItemId(item.getId());
        if (item != null && !transactions.isEmpty()) {
            Ufv currentUfv = ufvRepository.findByCreationDate(item.getInitialDate());
            item.setPrice(current.getItem().getPrice());
            item.setQuantity(transactions.get(0).getBalance());
            item.setLastUpdate(item.getInitialDate());
            itemRepository.save(item);
            Transaction transaction = new Transaction();

            transaction.setBalance(item.getQuantity());
            transaction.setPriceActual(item.getPrice());
            transaction.setPriceNeto(BigDecimal.ZERO);
            transaction.setType(INITIAL);
            transaction.setTransactionDate(item.getLastUpdate());
            transaction.setIncrement(BigDecimal.ZERO);
            transaction.setProcessId(transaction.getProcessId());

            transaction.setItem(item);
            transaction.setUfv(currentUfv);
            transactionRepository.save(transaction);
            transactions.forEach((b) -> {
                if (b.getType() != INITIAL) {
                    b.setProcessId(transaction.getProcessId());
                    b.setPriceNeto(transaction.getPriceActual());
                    b.setItem(item);
                    executeMoving(b);
                }
            });
        }
    }
}
