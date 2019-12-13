package com.manaco.org.operator;

import com.manaco.org.model.ItemStock;
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
                case ENTRY_BUY:
                    executeEntryBuy(item, transaction, actual);
                    break;
            }
            LOGGER.info("saved successfully with id " + transaction.getItem());
        } else {
            System.out.println("item not found with  id " + transaction.getItem());
            saveItemNotFound(transaction);
        }
    }

    private void executeEntryBuy(Item item, Transaction transaction, Ufv actual) {
        Transaction entry = new Transaction();
        entry.setType(TransactionType.ENTRY_BUY);
        entry.setTransactionDate(transaction.getTransactionDate());
        entry.setEntry(transaction.getItem().getQuantity());
        entry.setBalance(entry.getEntry().add(item.getQuantity()));
        entry.setPriceNeto(transaction.getPriceNeto());
        entry.setUfv(actual);
        entry.setTotalEntry(operator.calculateTotalItem(entry.getPriceNeto(), entry.getEntry()));
        entry.setTotalNormal(entry.getTotalEntry().add(item.getTotal()).setScale(6, BigDecimal.ROUND_CEILING));
        entry.setTotalUpdate(entry.getTotalEntry().add(item.getTotalUpdate()).setScale(6, BigDecimal.ROUND_CEILING));
        entry.setIncrement(BigDecimal.ZERO);
        //para obtener el nuevo precio
        if(entry.getTotalUpdate().intValue() <= 0) {
            entry.setPriceActual(item.getPrice());
        } else {
            entry.setPriceActual(operator.newPrice(entry.getTotalUpdate(), entry.getBalance(), item.getPrice()));
        }

        // update item
        item.setPrice(entry.getPriceActual());
        item.setQuantity(entry.getBalance());
        item.setTotal(entry.getTotalNormal());
        item.setTotalUpdate(entry.getTotalUpdate());
        // detail information
        entry.setProcessId(transaction.getProcessId());
        entry.setIdentifier(item.getIdentifier());
        entry.setInformation(transaction.getInformation());
        entry.setItem(item);
        // save data
        itemRepository.save(item);
        transactionRepository.save(entry);
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
        item.setTotalUpdate(BigDecimal.ZERO);
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

    public Item updateItem(Item item, Transaction transaction, Ufv actual) {
        if (!item.getLastUpdate().equals(transaction.getTransactionDate())) {
            if (item.getQuantity().intValue() > 0) {
                Ufv before = ufvRepository.findByCreationDate(item.getLastUpdate());
                BigDecimal totalUpdate = operator.calculateUpdate(item.getTotalUpdate(), actual.getValue(), before.getValue());
                BigDecimal newPrice = operator.newPrice(totalUpdate, item.getQuantity(), item.getPrice());
                BigDecimal increment = operator.caclulateUfvValue(totalUpdate, item.getTotalUpdate()).setScale(6, BigDecimal.ROUND_CEILING);
                item.setPrice(newPrice.setScale(6, BigDecimal.ROUND_CEILING));
                item.setLastUpdate(transaction.getTransactionDate());
                item.setTotalUpdate(totalUpdate.setScale(6, BigDecimal.ROUND_CEILING));
                item.setTotal(item.getTotal());
                saveMove(item, TransactionType.UPDATE, increment, item.getTotal(), totalUpdate,
                        actual, transaction.getProcessId());
            } else {
                item.setLastUpdate(transaction.getTransactionDate());
                saveMove(item, TransactionType.UPDATE, BigDecimal.ZERO, item.getTotal(), item.getTotalUpdate(),
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
        egress.setType(TransactionType.EGRESS);
        egress.setTransactionDate(transaction.getTransactionDate());
        egress.setEgress(transaction.getItem().getQuantity());
        egress.setBalance(item.getQuantity().subtract(egress.getEgress()).setScale(6, BigDecimal.ROUND_CEILING));
        egress.setPriceActual(item.getPrice());
        egress.setUfv(actual);
        egress.setTotalEgress(operator.calculateTotalItem(item.getPrice(), egress.getEgress()));
        egress.setTotalNormal(item.getTotal().subtract(egress.getTotalEgress()).setScale(6, BigDecimal.ROUND_CEILING));
        egress.setTotalUpdate(item.getTotalUpdate().subtract(egress.getTotalEgress()).setScale(6, BigDecimal.ROUND_CEILING));
        egress.setIncrement(BigDecimal.ZERO);
        //update item
        item.setQuantity(egress.getBalance());
        item.setTotal(egress.getTotalNormal());
        item.setTotalUpdate(egress.getTotalUpdate());
        // detail information
        egress.setItem(item);
        egress.setProcessId(transaction.getProcessId());
        egress.setIdentifier(item.getIdentifier());
        egress.setInformation(transaction.getInformation());
        //save item
        itemRepository.save(item);
        transactionRepository.save(egress);
    }

    private void executeEntry(Item item, Transaction transaction, Ufv actual) {
        Transaction entry = new Transaction();
        entry.setType(TransactionType.ENTRY);
        entry.setTransactionDate(transaction.getTransactionDate());
        entry.setEntry(transaction.getItem().getQuantity());
        entry.setBalance(entry.getEntry().add(item.getQuantity()));
        entry.setPriceNeto(transaction.getPriceNeto());
        entry.setUfv(actual);
        entry.setTotalEntry(operator.calculateTotalItem(entry.getPriceNeto(), entry.getEntry()));
        entry.setTotalNormal(entry.getTotalEntry().add(item.getTotal()).setScale(6, BigDecimal.ROUND_CEILING));
        entry.setTotalUpdate(entry.getTotalEntry().add(item.getTotalUpdate()).setScale(6, BigDecimal.ROUND_CEILING));
        entry.setIncrement(BigDecimal.ZERO);
        //para obtener el nuevo precio
        if(entry.getTotalUpdate().intValue() <= 0) {
            entry.setPriceActual(item.getPrice());
        } else {
            entry.setPriceActual(operator.newPrice(entry.getTotalUpdate(), entry.getBalance(), item.getPrice()));
        }

        // update item
        item.setPrice(entry.getPriceActual());
        item.setQuantity(entry.getBalance());
        item.setTotal(entry.getTotalNormal());
        item.setTotalUpdate(entry.getTotalUpdate());
        // detail information
        entry.setProcessId(transaction.getProcessId());
        entry.setIdentifier(item.getIdentifier());
        entry.setInformation(transaction.getInformation());
        entry.setItem(item);
        // save data
        itemRepository.save(item);
        transactionRepository.save(entry);
    }

    public void saveItem(Transaction transaction) {

//        if (transaction.getItem().getQuantity().intValue() < 0) {
//            transaction.getItem().setIsFailure(Boolean.TRUE);
//        } else {
//            transaction.getItem().setIsFailure(Boolean.FALSE);
//        }
        itemRepository.save(transaction.getItem());
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

    public void updateItemCierre(Item item, Transaction transaction, Ufv actual) {
//        if (!item.getLastUpdate().equals(transaction.getTransactionDate())) {
            if (item.getQuantity().intValue() > 0) {
                Ufv before = ufvRepository.findByCreationDate(item.getLastUpdate());
                BigDecimal totalUpdate = operator.calculateUpdate(item.getTotalUpdate(), actual.getValue(), before.getValue());
                BigDecimal newPrice = operator.newPrice(totalUpdate, item.getQuantity(), item.getPrice());
                BigDecimal increment = operator.caclulateUfvValue(totalUpdate, item.getTotalUpdate()).setScale(6, BigDecimal.ROUND_CEILING);
                item.setPrice(newPrice.setScale(6, BigDecimal.ROUND_CEILING));
                item.setLastUpdate(transaction.getTransactionDate());
                item.setTotalUpdate(totalUpdate.setScale(6, BigDecimal.ROUND_CEILING));
                item.setTotal(item.getTotal());
                saveMove(item, TransactionType.CIERRE, increment, item.getTotal(), totalUpdate,
                        actual, transaction.getProcessId());
            } else {
                item.setLastUpdate(transaction.getTransactionDate());
                saveMove(item, TransactionType.CIERRE, BigDecimal.ZERO, item.getTotal(), item.getTotalUpdate(),
                        actual, transaction.getProcessId());
            }
//        }
    }
}