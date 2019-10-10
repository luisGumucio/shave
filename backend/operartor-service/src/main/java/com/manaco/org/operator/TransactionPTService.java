package com.manaco.org.operator;

import com.manaco.org.model.ItemStock;
import com.manaco.org.model.*;
import com.manaco.org.repositories.*;
import com.manaco.org.utils.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.manaco.org.model.TransactionType.INITIAL;

@Service
public class TransactionPTService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionPTService.class);

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

    private List<Transaction> transactions;

    public TransactionPTService() {
        transactions = new ArrayList<>();
    }


    public synchronized void executeMoving(Transaction transaction) {
        Item item = itemRepository.findById(transaction.getItem().getId()).orElse(null);
//        LOGGER.info(transaction.getDetail().getInformation().get("ALMACEN"));
        ItemStock itemStock = null;
        if(item != null) {
            itemStock = itemStockRepository.findByStockIdAndItemId(transaction.getDetail().getInformation().get("ALMACEN"),
                    item.getId()).orElse(null);
        }

        if (item != null && itemStock != null) {

            Ufv actual = ufvRepository.findByCreationDate(transaction.getTransactionDate());
//            item = updateItem(item, transaction, actual);
            switch (transaction.getType()) {
                case ENTRY:
                    executeEntry(item, transaction, actual, null);
                    break;
                case EGRESS:
                    executeEgress(item, transaction, actual, null);
                    break;
                case CAM:
                    executeCam(item, transaction, actual, null);
                    break;
                case ENTRY_BUY:
                    executeEntryBuy(item, transaction, actual, null);
                    break;
            }
            LOGGER.info("saved successfully with id " + transaction.getItem().getId());
        } else {
            System.out.println("item not found with  id " + transaction.getItem().getId());
            saveItemNotFound(transaction, item);
        }
    }

    private void executeEntryBuy(Item item, Transaction transaction, Ufv actual, ItemStock o) {
        //        executeEntryByStock(item, transaction, actual, itemStock);
        Transaction entry = new Transaction();
        entry.setItem(item);
        entry.setType(TransactionType.G_ENTRY);
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
        entry.setDetail(transaction.getDetail());
        item.setPrice(entry.getPriceActual());
        item.setLastUpdate(transaction.getTransactionDate());
        detailRepository.save(transaction.getDetail());
        itemRepository.save(item);
        transactionRepository.save(entry);
    }

    private void executeCam(Item item, Transaction transaction, Ufv actual, ItemStock o) {
        Transaction entry = new Transaction();
        entry.setItem(item);
        entry.setType(TransactionType.CAM);
        entry.setTransactionDate(transaction.getTransactionDate());
        entry.setEntry(transaction.getItem().getQuantity());
        //calculando la entrada del item
        entry.setBalance(item.getQuantity().add(transaction.getItem().getQuantity()));
        entry.setPriceNeto(item.getPrice());
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
        entry.setDetail(transaction.getDetail());
        item.setLastUpdate(transaction.getTransactionDate());

        detailRepository.save(transaction.getDetail());
        itemRepository.save(item);
        transactionRepository.save(entry);
    }

    public void loadData(Transaction transaction) {
        LOGGER.info("Adding a new Transaction with item" + transaction.getItem().getId());
        transactions.add(transaction);
    }

    private Item updateItem(Item item, Transaction transaction, Ufv actual) {
        if (!item.getLastUpdate().equals(transaction.getTransactionDate())) {
            Ufv before = ufvRepository.findByCreationDate(item.getLastUpdate());
            if (item.getQuantity().intValue() != 0) {
                BigDecimal totalNormal = operator.calculateTotal(item.getQuantity(), item.getPrice());
                BigDecimal totalUpdate = operator.calculateUpdate(totalNormal, actual.getValue(), before.getValue());
                BigDecimal newPrice = operator.newPrice(totalUpdate, item.getQuantity());
                BigDecimal increment = operator.caclulateUfvValue(totalUpdate, totalNormal);
//                item.setPrice(newPrice.setScale(6, BigDecimal.ROUND_CEILING));
                item.setLastUpdate(transaction.getTransactionDate());
                saveMove(item, TransactionType.UPDATE, increment, totalNormal, totalUpdate,
                        actual, transaction.getProcessId(), transaction.getDetail(), newPrice);
            } else {
                item.setLastUpdate(transaction.getTransactionDate());
                saveMove(item, TransactionType.UPDATE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        actual, transaction.getProcessId(), transaction.getDetail(), item.getPrice());
            }
        }
        return item;
    }


    private void saveMove(Item item, TransactionType type, BigDecimal increment, BigDecimal totalNormal,
                          BigDecimal totalUpdate, Ufv actual,
                          String proccesID, TransactionDetail detail, BigDecimal newPrice) {

        List<ItemStock> itemStocks = itemStockRepository.findByItemId(item.getId());
        itemStocks.forEach(b -> {
            Transaction transaction = new Transaction();
            if (b.getQuantity().intValue() <= 0) {
                transaction.setBalance(b.getQuantity());
                transaction.setTotalNormal(totalNormal);
                transaction.setTotalUpdate(totalUpdate);
                transaction.setIncrement(BigDecimal.ZERO);
            } else {
                BigDecimal porcentaje = operator.calculatePorcentaje(b.getQuantity(), item.getQuantity());
                transaction.setBalance(b.getQuantity());
                transaction.setTotalNormal(b.getQuantity().multiply(item.getPrice()));
                transaction.setTotalUpdate(porcentaje.multiply(totalUpdate));
                transaction.setIncrement(transaction.getTotalUpdate().subtract(transaction.getTotalNormal()));
            }

            transaction.setItem(item);
            transaction.setType(type);
            transaction.setTransactionDate(item.getLastUpdate());
            transaction.setPriceActual(item.getPrice());
            transaction.setUfv(actual);

            transaction.setProcessId(proccesID);
            transaction.setIdentifier(item.getIdentifier());

            transactionRepository.save(transaction);
        });

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

        item.setPrice(newPrice);
        itemRepository.save(item);
        transactionRepository.save(transaction);
    }


    public void executeTransaction() {
        if (transactions.isEmpty()) {
            LOGGER.info("hola");
        } else {
            transactions.forEach(b -> {
                LOGGER.info(b.getItem().getId());
            });
        }
    }

    public void saveItemProduct(Transaction transaction) {
        if (transaction.getItem().getQuantity().intValue() < 0) {
            transaction.getItem().setIsFailure(Boolean.TRUE);
        } else {
            transaction.getItem().setIsFailure(Boolean.FALSE);
        }

        Item item = itemRepository.findById(transaction.getItem().getId()).orElse(null);

        if (item == null) {
            item = itemRepository.save(transaction.getItem());
        } else {
            item.setQuantity(item.getQuantity().add(transaction.getBalance()));
            item.setTotal(item.getQuantity().multiply(item.getPrice()));
            item = itemRepository.save(item);
        }

        saveStock(transaction, item);
        detailRepository.save(transaction.getDetail());
        transaction.setType(INITIAL);
        transactionRepository.save(transaction);
        LOGGER.info("adding initial transaction PT with item id" + transaction.getItem().getId());
    }

    private void saveItemStock(Stock stock, Transaction transaction, Item  item) {
        ItemStock itemStock =  itemStockRepository.findByStockIdAndItemId(stock.getId(), item.getId()).orElse(null);
        if(itemStock == null) {
            itemStock = new ItemStock();
            itemStock.setStock(stock);
            itemStock.setItem(item);
            itemStock.setQuantity(transaction.getBalance());
            itemStock.setTotal(itemStock.getQuantity().multiply(transaction.getItem().getPrice()));
        } else {
            itemStock.setQuantity(itemStock.getQuantity().add(transaction.getBalance()));
            itemStock.setTotal(itemStock.getQuantity().multiply(transaction.getItem().getPrice()));
        }

        itemStockRepository.save(itemStock);
    }

    private void saveStock(Transaction transaction, Item item) {
        String stockId = transaction.getDetail().getInformation().get("TIENDA");
        Stock stock = stockRepository.findById(stockId).orElse(null);
        if (stock == null) {
            stock = new Stock();
            stock.setId(stockId);
            stockRepository.save(stock);
        }
        saveItemStock(stock, transaction, item);
    }

    private void saveStockNotFound(Transaction transaction, Item item) {
        String stockId = transaction.getDetail().getInformation().get("ALMACEN");
        Stock stock = stockRepository.findById(stockId).orElse(null);
        if (stock == null) {
            stock = new Stock();
            stock.setId(stockId);
            stockRepository.save(stock);
        }
        saveItemStockNotFound(stock, transaction, item);
    }

    private void saveItemStockNotFound(Stock stock, Transaction transaction, Item  item) {
        ItemStock itemStock =  itemStockRepository.findByStockIdAndItemId(stock.getId(), item.getId()).orElse(null);
        if(itemStock == null) {
            itemStock = new ItemStock();
            itemStock.setStock(stock);
            itemStock.setItem(item);
            itemStock.setQuantity(transaction.getItem().getQuantity());
            itemStock.setTotal(itemStock.getQuantity().multiply(transaction.getItem().getPrice()));
        } else {
            itemStock.setQuantity(itemStock.getQuantity().add(transaction.getBalance()));
            itemStock.setTotal(itemStock.getQuantity().multiply(transaction.getItem().getPrice()));
        }

        itemStockRepository.save(itemStock);
    }

    private void saveItemNotFound(Transaction otherTransaction, Item item) {
        Transaction transaction = new Transaction();
        if(item == null) {
            item = new Item();
            item.setId(otherTransaction.getItem().getId());
            item.setPrice(otherTransaction.getItem().getPrice());
            item.setQuantity(BigDecimal.ZERO);
            LocalDate currentDate = otherTransaction.getTransactionDate();
            item.setInitialDate(currentDate);
            item.setLastUpdate(item.getInitialDate());
            item.setTotal(BigDecimal.ZERO);
        }
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
        transaction.setDetail(otherTransaction.getDetail());
        transaction.setIdentifier(otherTransaction.getItem().getIdentifier());
        if (transaction.getItem().getQuantity().intValue() < 0) {
            transaction.getItem().setIsFailure(Boolean.TRUE);
        } else {
            transaction.getItem().setIsFailure(Boolean.FALSE);
        }
        item.setIdentifier(transaction.getIdentifier());
        itemRepository.save(transaction.getItem());
        detailRepository.save(otherTransaction.getDetail());
        transactionRepository.save(transaction);
        LOGGER.info("adding initial transaction with item id: " + transaction.getItem().getId());
        saveStockNotFound(otherTransaction, item);
        executeMoving(otherTransaction);
    }

    private void executeEgress(Item item, Transaction transaction, Ufv actual, ItemStock itemStock) {
//        executeEgressByStock(item, transaction, actual, itemStock);
        Transaction egress = new Transaction();
        egress.setItem(item);
        egress.setType(TransactionType.G_EGRESS);
        egress.setTransactionDate(transaction.getTransactionDate());
        egress.setEgress(transaction.getItem().getQuantity());

        //calculando la salida del item
        if(transaction.getItem().getQuantity().compareTo(BigDecimal.ZERO) < 0) {
            egress.setBalance(item.getQuantity().subtract(transaction.getItem().getQuantity().negate()));
        } else {
            egress.setBalance(item.getQuantity().subtract(transaction.getItem().getQuantity()));
        }

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
        egress.setDetail(transaction.getDetail());
        item.setLastUpdate(transaction.getTransactionDate());
        detailRepository.save(transaction.getDetail());
        itemRepository.save(item);
        transactionRepository.save(egress);
    }

    private void executeEgressByStock(Item item, Transaction transaction, Ufv actual, ItemStock itemStock) {


        Transaction egress = new Transaction();
        egress.setItem(item);
        egress.setType(TransactionType.EGRESS);
        egress.setTransactionDate(transaction.getTransactionDate());
        egress.setEgress(transaction.getItem().getQuantity());

        //calculando la salida del item
        egress.setBalance(itemStock.getQuantity().subtract(transaction.getItem().getQuantity()));
        egress.setPriceActual(item.getPrice());
        egress.setTotalEgress(item.getPrice().multiply(transaction.getItem().getQuantity()));

        if (egress.getBalance().equals(BigDecimal.ZERO)) {
            egress.setTotalNormal(BigDecimal.ZERO);
            egress.setTotalUpdate(BigDecimal.ZERO);
            itemStock.setTotal(BigDecimal.ZERO);
            itemStock.setQuantity(BigDecimal.ZERO);
        } else {
            itemStock.setTotal(itemStock.getTotal().subtract(egress.getTotalEgress()));
            egress.setTotalNormal(itemStock.getTotal());
            BigDecimal itemQuantityTotal = operator.calculateQuantityTotal(itemStock.getQuantity(), item.getPrice());
            egress.setTotalUpdate(itemQuantityTotal.subtract(egress.getTotalEgress()));
            itemStock.setQuantity(egress.getBalance());
        }

        egress.setUfv(actual);
        egress.setIncrement(BigDecimal.ZERO);
        egress.setProcessId(transaction.getProcessId());
        egress.setIdentifier(item.getIdentifier());
        egress.setDetail(transaction.getDetail());
//        item.setLastUpdate(transaction.getTransactionDate());
        detailRepository.save(transaction.getDetail());
        transactionRepository.save(egress);
        itemStockRepository.save(itemStock);
    }

    private void executeEntry(Item item, Transaction transaction, Ufv actual, ItemStock itemStock) {
//        executeEntryByStock(item, transaction, actual, itemStock);
        Transaction entry = new Transaction();
        entry.setItem(item);
        entry.setType(TransactionType.G_ENTRY);
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
        entry.setDetail(transaction.getDetail());
        item.setPrice(entry.getPriceActual());
        item.setLastUpdate(transaction.getTransactionDate());
        detailRepository.save(transaction.getDetail());
        itemRepository.save(item);
        transactionRepository.save(entry);
    }

    private void executeEntryByStock(Item item, Transaction transaction, Ufv actual, ItemStock itemStock) {
        executeEgressByStock(item, transaction, actual, itemStock);
        Transaction entry = new Transaction();
        entry.setItem(item);
        entry.setType(TransactionType.ENTRY);
        entry.setTransactionDate(transaction.getTransactionDate());
        entry.setEntry(transaction.getItem().getQuantity());
        //calculando la entrada del item
        entry.setBalance(itemStock.getQuantity().add(transaction.getItem().getQuantity()));
        entry.setPriceNeto(transaction.getPriceActual());
        entry.setPriceActual(item.getPrice());
        entry.setTotalEntry(transaction.getItem().getPrice().multiply(transaction.getItem().getQuantity()));
        itemStock.setTotal(itemStock.getTotal().add(entry.getTotalEntry()));
        entry.setTotalNormal(itemStock.getTotal());
        BigDecimal itemQuantityTotal = operator.calculateQuantityTotal(itemStock.getQuantity(), item.getPrice());
        entry.setTotalUpdate(itemQuantityTotal.add(entry.getTotalEntry()));
        itemStock.setQuantity(entry.getBalance());
        entry.setUfv(actual);
        entry.setIncrement(BigDecimal.ZERO);
        entry.setProcessId(transaction.getProcessId());
        entry.setIdentifier(item.getIdentifier());
        entry.setDetail(transaction.getDetail());
        detailRepository.save(transaction.getDetail());
//        itemRepository.save(item);
        transactionRepository.save(entry);
        itemStockRepository.save(itemStock);
    }
}
