package com.manaco.org.operator;

import com.manaco.org.model.*;

//import static com.manaco.org.model.TransactionOption.UPDATE_PROCESS;

import com.manaco.org.repositories.*;
import com.manaco.org.utils.Operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

//import javax.transaction.Transactional;

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
    protected MongoTemplate mongoTemplate;


    public void saveItem(Transaction transaction) {

        if (transaction.getItem().getQuantity().intValue() < 0) {
            transaction.getItem().setIsFailure(Boolean.TRUE);
        } else {
            transaction.getItem().setIsFailure(Boolean.FALSE);
        }

        itemRepository.save(transaction.getItem());
        detailRepository.save(transaction.getDetail());
        transactionRepository.save(transaction);
        savePrimaStock(transaction, transaction.getItem());
        LOGGER.info("adding initial transaction with item id" + transaction.getItem().getId());
    }

    private synchronized Ufv checkUfv(Ufv ufv) {
        Ufv actual = ufvRepository.findByCreationDate(ufv.getCreationDate());
        if (actual == null) {
            ufvRepository.save(ufv);
        }
        return actual;
    }

    public void saveItemProduct(Transaction transaction) {
        if (transaction.getItem().getQuantity().intValue() < 0) {
            transaction.getItem().setIsFailure(Boolean.TRUE);
        } else {
            transaction.getItem().setIsFailure(Boolean.FALSE);
        }

        Item item = itemRepository.findById(transaction.getItem().getId()).orElse(null);

        if(item == null) {
            itemRepository.save(transaction.getItem());
        } else {
            item.setQuantity(item.getQuantity().add(transaction.getQuantity()));
            itemRepository.save(item);
        }
        saveStock(transaction, item);
        detailRepository.save(transaction.getDetail());
        transactionRepository.save(transaction);
        LOGGER.info("adding initial transaction with item id" + transaction.getItem().getId());
    }

    private void saveStock(Transaction transaction, Item item) {
        Stock stock = stockRepository.findById(Long.valueOf(transaction.getDetail().getInformation().get("TIENDA"))).orElse(null);
        if(stock == null && item == null) {
            stock = new Stock();
            stock.setId(Long.valueOf(transaction.getDetail().getInformation().get("TIENDA")));
            stock.setQuantity(transaction.getQuantity());
            stock.addItem(transaction.getItem());
            stockRepository.save(stock);
        } else if(stock == null && item != null) {
            stock = new Stock();
            stock.setId(Long.valueOf(transaction.getDetail().getInformation().get("TIENDA")));
            stock.setQuantity(transaction.getQuantity());
            stock.addItem(item);
            stockRepository.save(stock);
        } else {
            stock.setQuantity(stock.getQuantity().add(transaction.getQuantity()));
            stock.addItem(item);
            stockRepository.save(stock);
        }
    }

    private void savePrimaStock(Transaction transaction, Item item) {
        Stock stock = stockRepository.findById(Long.valueOf(transaction.getDetail().getInformation().get("ALMACEN"))).orElse(null);
        if(stock == null && item == null) {
            stock = new Stock();
            stock.setId(Long.valueOf(transaction.getDetail().getInformation().get("ALMACEN")));
            stock.setQuantity(transaction.getQuantity());
            stock.addItem(transaction.getItem());
            stockRepository.save(stock);
        } else if(stock == null && item != null) {
            stock = new Stock();
            stock.setId(Long.valueOf(transaction.getDetail().getInformation().get("ALMACEN")));
            stock.setQuantity(transaction.getQuantity());
            stock.addItem(item);
            stockRepository.save(stock);
        } else {
            stock.setQuantity(stock.getQuantity().add(transaction.getQuantity()));
            stock.addItem(item);
            stockRepository.save(stock);
        }
    }

    public void executeMoving(Transaction transaction) {
        Item item = itemRepository.findById(transaction.getItem().getId()).orElse(null);
        if (item != null) {
            Ufv actual = ufvRepository.findByCreationDate(transaction.getTransactionDate());
            updateItem(item, transaction, actual);
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
        }
    }
//
//    void saveMoving(Transaction transaction) {
//        Item item = itemRepository.findById(transaction.getTransactionDetail().getItem().getId()).orElse(null);
//        if (item != null) {
//            Ufv actual = ufvRepository.findByCreationDate(transaction.getTransactionDate());
//            updateItem(item, transaction, actual);
//            switch (transaction.getType()) {
//                case ENTRY:
//                    executeEntry(item, transaction, actual);
//                    break;
//                case EGRESS:
//                    executeEgress(item, transaction, actual);
//                    break;
//            }
//            System.out.println("saved successfully with id " + transaction.getTransactionDetail().getItem().getId());
//        } else {
//            System.out.println("item not found with  id " + transaction.getTransactionDetail().getItem().getId());
//            Item actual = saveItemInit(transaction);
//            transaction.getTransactionDetail().setItem(actual);
//            saveMoving(transaction);
//        }
//    }

    private Item updateItem(Item item, Transaction transaction, Ufv actual) {
        if (!item.getLastUpdate().equals(transaction.getTransactionDate())) {
            Ufv before = ufvRepository.findByCreationDate(item.getLastUpdate());
            if (item.getQuantity().intValue() != 0) {
                BigDecimal totalNormal = operator.calculateTotal(item.getQuantity(), item.getPrice());
                BigDecimal totalUpdate = operator.calculateUpdate(totalNormal, actual.getValue(), before.getValue());
                BigDecimal newPrice = operator.newPrice(totalUpdate, item.getQuantity());
                BigDecimal ufvValue = operator.caclulateUfvValue(totalUpdate, totalNormal);
                item.setPrice(newPrice.setScale(6, BigDecimal.ROUND_CEILING));
                item.setLastUpdate(transaction.getTransactionDate());
                saveMove(item, TransactionType.UPDATE, ufvValue, actual, transaction.getProcessId());
            }
        }
        return item;
    }
//
    private void saveMove(Item item, TransactionType type, BigDecimal ufvValue, Ufv ufv, String proccesID) {
        Transaction transaction = new Transaction();
        transaction.setPriceActual(item.getPrice());
        transaction.setQuantity(item.getQuantity());
        transaction.setType(type);
        transaction.setUfvValue(ufvValue);
        transaction.setTransactionDate(item.getLastUpdate());
        transaction.setProcessId(proccesID);
        itemRepository.save(item);
        transactionRepository.save(transaction);
    }
//
    private void executeEntry(Item item, Transaction transaction, Ufv actual) {
        Transaction entry = new Transaction();
        entry.setType(TransactionType.ENTRY);
        entry.setPriceNeto(transaction.getPriceNeto());
        BigDecimal quantityTotal = item.getQuantity().add(transaction.getQuantity().setScale(6, BigDecimal.ROUND_CEILING));
        if (quantityTotal.intValue() != 0) {
            BigDecimal totalNormal = operator.calculateTotal(transaction.getQuantity(), transaction.getPriceNeto());
            BigDecimal totalUpdate = operator.calculateTotal(item.getQuantity(), item.getPrice());
            BigDecimal newPrice = operator.newPrice(totalUpdate.add(totalNormal), quantityTotal);
            entry.setPriceActual(newPrice.setScale(6, BigDecimal.ROUND_CEILING));
        } else {
            entry.setPriceActual(item.getPrice());
        }

        entry.setQuantity(transaction.getQuantity().setScale(6, BigDecimal.ROUND_CEILING));
        entry.setTransactionDate(transaction.getTransactionDate());
//        entry.setTransactionDetail(detail);
        entry.setUfvValue(BigDecimal.ZERO);
        entry.setProcessId(transaction.getProcessId());
        item.setPrice(entry.getPriceActual());
        item.setQuantity(quantityTotal);
//        detail.setUfv(actual);
//        detail.setItem(item);
        itemRepository.save(item);
        transactionRepository.save(entry);
    }
//
    private void executeEgress(Item item, Transaction transaction, Ufv actual) {
        Transaction egress = new Transaction();
//        TransactionDetail detail = transaction.getTransactionDetail();
        egress.setType(TransactionType.EGRESS);
        egress.setPriceActual(item.getPrice());
        egress.setQuantity(transaction.getQuantity());

//        egress.setTransactionDetail(detail);
        egress.setTransactionDate(transaction.getTransactionDate());
        egress.setUfvValue(new BigDecimal(0));
        egress.setProcessId(transaction.getProcessId());
        item.setQuantity(item.getQuantity().subtract(transaction.getQuantity()));
        item.setLastUpdate(transaction.getTransactionDate());
//        detail.setUfv(actual);
//        detail.setItem(item);
        itemRepository.save(item);
        transactionRepository.save(egress);
    }
//
//    public void executeItem(Item actual) {
//        Item item = itemRepository.findById(actual.getId()).orElse(null);
//        List<Transaction> transactions = transactionRepository.findByTransactionDetailItemItemIdOrderByDateAsc(actual.getId());
//        if (item != null && !transactions.isEmpty()) {
//            Ufv current = ufvRepository.findByCreationDate(item.getInitialDate());
//            Process process = proccessRepository.findByNumberProcessAndActiveIn(2, true);
//            item.setPrice(actual.getPrice());
//            item.setQuantity(transactions.get(0).getQuantity());
//            item.setLastUpdate(item.getInitialDate());
//            itemRepository.save(item);
//            Transaction transaction = new Transaction();
//            MateriaDetail detail = new MateriaDetail();
//
//            transaction.setQuantity(item.getQuantity());
//            transaction.setPriceActual(item.getPrice());
//            transaction.setPriceNeto(BigDecimal.ZERO);
//            transaction.setType(TransactionType.INITIAL);
//            transaction.setTransactionDate(item.getLastUpdate());
//            transaction.setUfvValue(BigDecimal.ZERO);
//            transaction.setProcessId(process.getId());
//
//            detail.setItem(item);
//            detail.setUfv(current);
//            transaction.setTransactionDetail(detail);
//            transactionRepository.save(transaction);
//            transactions.forEach((b) -> {
//                if (b.getType() != INITIAL) {
//                    b.setProcessId(process.getId());
//                    b.setPriceNeto(transaction.getPriceActual());
//                    saveMoving(b, item);
//                }
//            });
//        }
//    }
//
//
//    public void updateItem(LocalDate localDate) {
//        Ufv actual = ufvRepository.findByCreationDate(localDate);
//        Process proccess = new Process();
//        proccess.setNumberProcess(3);
//        proccess.setType(UPDATE_PROCESS);
//        proccessRepository.save(proccess);
//        List<Item> items = itemRepository.findAll();
//        items.forEach((item) -> {
//            updateItem(item, actual, localDate, proccess);
//        });
//    }
//
//    private void updateItem(Item item, Ufv actual, LocalDate date, Process proccess) {
//        Ufv before = ufvRepository.findByCreationDate(item.getLastUpdate());
//        if (item.getQuantity().intValue() != 0) {
//            BigDecimal totalNormal = operator.calculateTotal(item.getQuantity(), item.getPrice());
//            BigDecimal totalUpdate = operator.calculateUpdate(totalNormal, actual.getValue(), before.getValue());
//            BigDecimal newPrice = operator.newPrice(totalUpdate, item.getQuantity());
//            BigDecimal ufvValue = operator.caclulateUfvValue(totalUpdate, totalNormal);
//            item.setPrice(newPrice.setScale(6, BigDecimal.ROUND_CEILING));
//            item.setLastUpdate(date);
//            saveMove(item, TransactionType.UPDATE, ufvValue, actual, proccess.getId());
//        } else {
//            saveMove(item, TransactionType.UPDATE, BigDecimal.ZERO, actual, proccess.getId());
//        }
//    }

    public void pushMethod(Long objectId, Object... events) {
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(objectId)),
                new Update().push("items", events), Stock.class);
    }

}
