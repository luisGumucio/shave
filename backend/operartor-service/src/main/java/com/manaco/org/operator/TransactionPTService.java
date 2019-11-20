package com.manaco.org.operator;

import com.manaco.org.model.ItemStock;
import com.manaco.org.model.*;
import com.manaco.org.model.Process;
import com.manaco.org.repositories.*;
import com.manaco.org.utils.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.manaco.org.model.TransactionType.*;

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
    private StockRepository stockRepository;

    @Autowired
    private ItemStockRepository itemStockRepository;

    private List<Transaction> transactions;

    public TransactionPTService() {
        transactions = new ArrayList<>();
    }


    public void executeMoving(Transaction transaction, Boolean isNotFound) {
        Item item = itemRepository.findById(transaction.getItem().getId()).orElse(null);
        ItemStock itemStock = null;
        if(item != null) {
            if (!transaction.getInformation().get("Almacen").equals("00680")) {
                itemStock = itemStockRepository.findByStockIdAndItemId(transaction.getInformation().get("Almacen"),
                        item.getId()).orElse(null);
            } else {
                itemStock = new ItemStock();
                itemStock.setQuantity(BigDecimal.ZERO);
            }
        }
        if (item != null && itemStock != null) {
            Ufv actual = ufvRepository.findByCreationDate(transaction.getTransactionDate());
            item = updateItem(item, transaction, actual);
            switch (transaction.getType()) {
                case ENTRY:
                    executeEntry(item, transaction, actual, itemStock, isNotFound);
                    break;
                case EGRESS:
                    executeEgress(item, transaction, actual, itemStock, isNotFound);
                    break;
                case CAM:
                    executeCam(item, transaction, actual, itemStock);
                    break;
                case ENTRY_BUY:
                    executeEntryBuy(item, transaction, actual, itemStock, isNotFound);
                    break;
            }
            LOGGER.info("saved successfully with id " + transaction.getItem().getId());
        } else {
            System.out.println("item not found with  id " + transaction.getItem().getId());
            saveItemNotFound(transaction, item);
        }
    }

    private void executeEntryBuy(Item item, Transaction transaction, Ufv actual, ItemStock itemStock,
                                 Boolean isNotFound) {
//        executeEntryBuyByStock(item, transaction, actual, itemStock, isNotFound);
        Transaction entry = new Transaction();
        entry.setItem(item);
        entry.setType(TransactionType.G_ENTRY_BUY);
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

    private void executeEntryBuyByStock(Item item, Transaction transaction, Ufv actual, ItemStock itemStock, Boolean isNotFound) {
        Transaction entry = new Transaction();
        entry.setItem(item);
        entry.setType(TransactionType.ENTRY_BUY);
        entry.setTransactionDate(transaction.getTransactionDate());
        entry.setEntry(transaction.getItem().getQuantity());
        //calculando la entrada del item
        if(isNotFound) {
            entry.setBalance(itemStock.getQuantity());
        } else {
            entry.setBalance(itemStock.getQuantity().add(transaction.getItem().getQuantity()));
        }
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
        entry.setInformation(transaction.getInformation());
        transactionRepository.save(entry);
        itemStockRepository.save(itemStock);
    }

    private Item updateItem(Item item, Transaction transaction, Ufv actual) {
        if (!item.getLastUpdate().equals(transaction.getTransactionDate())) {
            Ufv before = ufvRepository.findByCreationDate(item.getLastUpdate());
            if (item.getQuantity().intValue() > 0) {
                BigDecimal totalNormal = operator.calculateTotal(item.getQuantity(), item.getPrice());
                BigDecimal totalUpdate = operator.calculateUpdate(totalNormal, actual.getValue(), before.getValue());
                BigDecimal newPrice = operator.newPrice(totalUpdate, item.getQuantity());
                BigDecimal increment = operator.caclulateUfvValue(totalUpdate, totalNormal);
//                item.setPrice(newPrice.setScale(6, BigDecimal.ROUND_CEILING));
                item.setLastUpdate(transaction.getTransactionDate());
                saveMove(item, TransactionType.G_UPDATE, increment, totalNormal, totalUpdate,
                        actual, transaction.getProcessId(), newPrice);
            } else {
                item.setLastUpdate(transaction.getTransactionDate());
                saveMove(item, TransactionType.G_UPDATE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        actual, transaction.getProcessId(),  item.getPrice());
            }
        }
        return item;
    }


    private void saveMove(Item item, TransactionType type, BigDecimal increment, BigDecimal totalNormal,
                          BigDecimal totalUpdate, Ufv actual,
                          String proccesID, BigDecimal newPrice) {

//        List<ItemStock> itemStocks = itemStockRepository.findByItemId(item.getId());
//        itemStocks.forEach(b -> {
//            Transaction transaction = new Transaction();
//            if (b.getQuantity().intValue() <= 0 || item.getQuantity().intValue() <= 0) {
//                transaction.setBalance(b.getQuantity());
//                transaction.setTotalNormal(totalNormal);
//                transaction.setTotalUpdate(totalUpdate);
//                transaction.setIncrement(BigDecimal.ZERO);
//            } else {
//                BigDecimal porcentaje = operator.calculatePorcentaje(b.getQuantity(), item.getQuantity());
//                transaction.setBalance(b.getQuantity());
//                transaction.setTotalNormal(b.getQuantity().multiply(item.getPrice()));
//                transaction.setTotalUpdate(porcentaje.multiply(totalUpdate));
//                transaction.setIncrement(transaction.getTotalUpdate().subtract(transaction.getTotalNormal()));
//            }
//
//            transaction.setItem(item);
//            transaction.setType(UPDATE);
//            transaction.setTransactionDate(item.getLastUpdate());
//            transaction.setPriceActual(item.getPrice());
//            transaction.setUfv(actual);
//
//            transaction.setProcessId(proccesID);
//            transaction.setIdentifier(item.getIdentifier());
//
//            transactionRepository.save(transaction);
//        });

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


    public void saveItemProduct(Transaction transaction) {
        Item item = itemRepository.findById(transaction.getItem().getId()).orElse(null);
        if (item == null) {
            item = itemRepository.save(transaction.getItem());
        } else {
            if(!transaction.getInformation().get("Almacen").equals("00680")) {
                item.setQuantity(item.getQuantity().add(transaction.getBalance()));
                item.setTotal(item.getQuantity().multiply(item.getPrice()));
                item = itemRepository.save(item);
            }
        }
        saveStock(transaction, item);
        LOGGER.info("adding initial transaction PT with item id" + transaction.getItem().getId());
    }

    private void saveItemStock(Stock stock, Transaction transaction, Item  item) {
        ItemStock itemStock =  itemStockRepository.findByStockIdAndItemId(stock.getId(), item.getId()).orElse(null);
        if(stock.getId().equals("00680")) {
            itemStock = new ItemStock();
            itemStock.setStock(stock);
            itemStock.setItem(item);
            itemStock.setQuantity(transaction.getBalance());
            itemStock.setDate(transaction.getTransactionDate());
        } else {
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
        }

        Transaction other = new Transaction();
        other.setInformation(transaction.getInformation());
        other.setType(INITIAL);
        other.setPriceActual(item.getPrice());
        other.setPriceNeto(BigDecimal.ZERO);
        other.setBalance(itemStock.getQuantity());
        other.setTransactionDate(item.getInitialDate());
        other.setItem(item);
        other.setTotalNormal(itemStock.getTotal());
        other.setTotalUpdate(itemStock.getTotal());
        other.setIncrement(BigDecimal.ZERO);
        other.setProcessId(transaction.getProcessId());
        other.setUfv(ufvRepository.findByCreationDate(item.getLastUpdate()));
        other.setIdentifier(transaction.getIdentifier());
        itemStockRepository.save(itemStock);
        transactionRepository.save(other);

        Transaction transactionFound =  transactionRepository.findByItemIdAndType(transaction.getItem().getId(), "G_INITIAL")
                .orElse(null);
        if(transactionFound == null) {
            transaction.setType(G_INITIAL);
            transaction.setInformation(null);
            transactionRepository.save(transaction);
        } else {
            transactionFound.setBalance(item.getQuantity());
            transactionFound.setTotalNormal(item.getTotal());
            transactionFound.setTotalUpdate(item.getTotal());
            transactionRepository.save(transactionFound);
        }
    }

    private void saveStock(Transaction transaction, Item item) {
        String stockId = transaction.getInformation().get("Almacen");
        Stock stock = stockRepository.findById(stockId).orElse(null);
        if (stock == null) {
            stock = new Stock();
            stock.setId(stockId);
            stockRepository.save(stock);
        }
        saveItemStock(stock, transaction, item);
    }

    private void saveStockNotFound(Transaction transaction, Item item) {
        String stockId = transaction.getInformation().get("Almacen");
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

        if(stock.getId().equals("00680")) {
            itemStock = new ItemStock();
            itemStock.setStock(stock);
            itemStock.setItem(item);
            itemStock.setQuantity(transaction.getItem().getQuantity());
            itemStock.setTotal(itemStock.getQuantity().multiply(transaction.getItem().getPrice()));
        } else {
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
        transaction.setInformation(otherTransaction.getInformation());
        transaction.setIdentifier(otherTransaction.getItem().getIdentifier());
        if (transaction.getItem().getQuantity().intValue() < 0) {
            transaction.getItem().setIsFailure(Boolean.TRUE);
        } else {
            transaction.getItem().setIsFailure(Boolean.FALSE);
        }
        item.setIdentifier(transaction.getIdentifier());
        itemRepository.save(transaction.getItem());

        transactionRepository.save(transaction);
        LOGGER.info("adding initial transaction with item id: " + transaction.getItem().getId());
        saveStockNotFound(otherTransaction, item);
        executeMoving(otherTransaction, true);
    }

    private void executeEgress(Item item, Transaction transaction, Ufv actual, ItemStock itemStock,
                               boolean isNotFound) {
//        executeEgressByStock(item, transaction, actual, itemStock, isNotFound);
        Transaction egress = new Transaction();
        egress.setItem(item);
        egress.setType(TransactionType.G_EGRESS);
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

    private void executeEgressByStock(Item item, Transaction transaction, Ufv actual, ItemStock itemStock,
                                      boolean isNotFound) {
        Transaction egress = new Transaction();
        egress.setItem(item);
        egress.setType(TransactionType.EGRESS);
        egress.setTransactionDate(transaction.getTransactionDate());
        egress.setEgress(transaction.getItem().getQuantity());

        //calculando la salida del item
        if(isNotFound) {
            egress.setBalance(itemStock.getQuantity());
        } else {
            egress.setBalance(itemStock.getQuantity().subtract(transaction.getItem().getQuantity()));
        }

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
        egress.setInformation(transaction.getInformation());
        transactionRepository.save(egress);
        itemStockRepository.save(itemStock);
    }

    private void executeEntry(Item item, Transaction transaction, Ufv actual, ItemStock itemStock,
                              Boolean isNotFound) {
//        executeEntryByStock(item, transaction, actual, itemStock, isNotFound);
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
        entry.setInformation(transaction.getInformation());
        item.setPrice(entry.getPriceActual());
        item.setLastUpdate(transaction.getTransactionDate());
        itemRepository.save(item);
        transactionRepository.save(entry);
    }

    private void executeEntryByStock(Item item, Transaction transaction, Ufv actual, ItemStock itemStock,
                                     Boolean isNotFound) {
        Transaction entry = new Transaction();
        entry.setItem(item);
        entry.setType(TransactionType.ENTRY);
        entry.setTransactionDate(transaction.getTransactionDate());
        entry.setEntry(transaction.getItem().getQuantity());
        //calculando la entrada del item
        if(isNotFound) {
            entry.setBalance(itemStock.getQuantity());
        } else {
            entry.setBalance(itemStock.getQuantity().add(transaction.getItem().getQuantity()));
        }
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
        entry.setInformation(transaction.getInformation());
        transactionRepository.save(entry);
        itemStockRepository.save(itemStock);
    }


    private void executeCam(Item item, Transaction transaction, Ufv actual, ItemStock o) {
        executeCamByItem(item, transaction, actual, o);
        Transaction entry = new Transaction();
        entry.setItem(item);
        entry.setType(TransactionType.G_CAM);
        entry.setTransactionDate(transaction.getTransactionDate());
        entry.setEntry(transaction.getItem().getQuantity());
        //calculando la entrada del item
        entry.setBalance(transaction.getItem().getQuantity());
        entry.setUfv(actual);
        entry.setIncrement(BigDecimal.ZERO);
        entry.setProcessId(transaction.getProcessId());
        entry.setIdentifier(item.getIdentifier());
        entry.setInformation(transaction.getInformation());
        item.setLastUpdate(transaction.getTransactionDate());

        itemRepository.save(item);
        transactionRepository.save(entry);

    }

    private void executeCamByItem(Item item, Transaction transaction, Ufv actual, ItemStock itemStock) {
        Transaction entry = new Transaction();
        entry.setItem(item);
        entry.setType(TransactionType.CAM);
        entry.setTransactionDate(transaction.getTransactionDate());
        entry.setEntry(transaction.getItem().getQuantity());
        //calculando la entrada del item
        entry.setBalance(transaction.getItem().getQuantity());
        entry.setUfv(actual);
        entry.setIncrement(BigDecimal.ZERO);
        entry.setProcessId(transaction.getProcessId());
        entry.setIdentifier(item.getIdentifier());
        entry.setInformation(transaction.getInformation());
        item.setLastUpdate(transaction.getTransactionDate());

        String stockId = transaction.getInformation().get("Almacen");
        Stock stock = stockRepository.findById(stockId).orElse(null);

        ItemStock other = new ItemStock();
        other.setStock(stock);
        other.setItem(item);
        other.setQuantity(transaction.getItem().getQuantity());
        other.setDate(transaction.getTransactionDate());

        itemStockRepository.save(other);
        transactionRepository.save(entry);
    }

    public void udpateItem(Transaction transaction) {
        List<ItemStock> itemStocks = itemStockRepository.findByItemId(transaction.getItem().getId());
        ItemStock itemStock = itemStocks.get(itemStocks.size() -1);

        Process process = proccessRepository.findByIsActive(true);

        Ufv actual = ufvRepository.findByCreationDate(transaction.getTransactionDate());
        Ufv before = ufvRepository.findByCreationDate(transaction.getItem().getLastUpdate());

        if (transaction.getItem().getQuantity().intValue() != 0 || itemStock.getQuantity().intValue() != 0) {
            if(transaction.getItem().getQuantity().intValue() <= 0) {
                transaction.getItem().setQuantity(itemStock.getQuantity());
            } else {
                transaction.getItem().setQuantity(transaction.getItem().getQuantity().add(itemStock.getQuantity()));
            }

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
            saveMoveUpdate(transaction.getItem(), TransactionType.G_CIERRE, increment, totalNormal, totalUpdate,
                    actual, transaction.getProcessId());
        } else {
            transaction.getItem().setLastUpdate(transaction.getTransactionDate());
            saveMoveUpdate(transaction.getItem(), TransactionType.G_CIERRE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    actual, transaction.getProcessId());
        }
    }

    private void saveMoveUpdate(Item item, TransactionType type, BigDecimal increment, BigDecimal totalNormal,
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
