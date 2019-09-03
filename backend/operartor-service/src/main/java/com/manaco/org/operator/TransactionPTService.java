package com.manaco.org.operator;

import com.manaco.org.dto.ItemStock;
import com.manaco.org.model.*;
import com.manaco.org.repositories.*;
import com.manaco.org.utils.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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


    public void executeMoving(Transaction transaction) {
        Item item = itemRepository.findById(transaction.getItem().getId()).orElse(null);
        if (item != null) {
            Ufv actual = ufvRepository.findByCreationDate(transaction.getTransactionDate());
            item = updateItem(item, transaction, actual);
            switch (transaction.getType()) {
                case ENTRY:
//                    executeEntry(item, transaction, actual);
                    break;
                case EGRESS:
//                    executeEgress(item, transaction, actual);
                    break;
            }
            LOGGER.info("saved successfully with id " + transaction.getItem());
        } else {
            System.out.println("item not found with  id " + transaction.getItem());
//            saveItemNotFound(transaction);
        }
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
                        actual, transaction.getProcessId(), transaction.getDetail());
            } else {
                item.setLastUpdate(transaction.getTransactionDate());
                saveMove(item, TransactionType.UPDATE, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        actual, transaction.getProcessId(), transaction.getDetail());
            }
        }
        return item;
    }


    private void saveMove(Item item, TransactionType type, BigDecimal increment, BigDecimal totalNormal,
                          BigDecimal totalUpdate, Ufv actual,
                          String proccesID, TransactionDetail detail) {

        List<Stock> stocks = stockRepository.findByItemsItemId(item.getId());
        long stockId = Long.parseLong(detail.getInformation().get("TIENDA"));
        itemRepository.save(item);
        stocks.forEach(b -> {
            ItemStock itemStock = b.getItems()
                    .stream().filter(c -> c.getItem().getId() == item.getId()).findAny().get();
            Transaction transaction = new Transaction();

            if(stockId == b.getId()) {
                transaction.setDetail(detail);
            }

            if(item.getQuantity().intValue() == 0) {
                transaction.setBalance(item.getQuantity());
                transaction.setTotalNormal(totalNormal);
                transaction.setTotalUpdate(totalUpdate);
            } else {
                BigDecimal porcentaje = operator.calculatePorcentaje(itemStock.getQuantity(), item.getQuantity());
                transaction.setBalance(itemStock.getQuantity());
                transaction.setTotalNormal(itemStock.getQuantity().multiply(item.getPrice()));
                transaction.setTotalUpdate(porcentaje.multiply(totalUpdate));
            }

            transaction.setItem(item);
            transaction.setType(type);
            transaction.setTransactionDate(item.getLastUpdate());
            transaction.setPriceActual(item.getPrice());
            transaction.setUfv(actual);
            transaction.setIncrement(increment);
            transaction.setProcessId(proccesID);
            transaction.setIdentifier(item.getIdentifier());
            transactionRepository.save(transaction);
        });
    }



//    private void updateEntryStock(Map<String, String> information, BigDecimal currentQuantity) {
//        Stock stock = stockRepository.findById(Long.valueOf(information.get("TIENDA"))).get();
//        stock.setQuantity(currentQuantity.add(stock.getQuantity()));
//        stockRepository.save(stock);
//    }

    private void executeEntry(Item item, Transaction transaction, Ufv actual) {
        long stockId = Long.parseLong(transaction.getDetail().getInformation().get("TIENDA"));
        Stock stock = stockRepository.findById(stockId).get();

        Transaction entry = new Transaction();
        entry.setItem(item);
        entry.setType(TransactionType.ENTRY);
        entry.setTransactionDate(transaction.getTransactionDate());
        entry.setEntry(transaction.getItem().getQuantity());
        //calculando la entrada del item
        entry.setPriceNeto(transaction.getPriceActual());
        entry.setPriceActual(item.getPrice());

        BigDecimal totalItem = item.getQuantity().add(transaction.getItem().getQuantity());
        entry.setBalance(item.getQuantity().add(transaction.getItem().getQuantity()));
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
        item.setPrice(entry.getPriceActual());
        item.setLastUpdate(transaction.getTransactionDate());

        itemRepository.save(item);
        transactionRepository.save(entry);
    }
}
