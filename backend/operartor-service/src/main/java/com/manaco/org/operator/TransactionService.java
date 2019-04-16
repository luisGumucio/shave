package com.manaco.org.operator;

import com.manaco.org.common.model.Item;
import com.manaco.org.common.model.MateriaDetail;
import com.manaco.org.common.model.Proccess;
import com.manaco.org.common.model.Transaction;
import com.manaco.org.common.model.TransactionType;

import static com.manaco.org.common.model.TransactionOption.UPDATE_PROCESS;
import static com.manaco.org.common.model.TransactionType.INITIAL;

import com.manaco.org.common.model.Ufv;
import com.manaco.org.common.repositories.ItemRepository;
import com.manaco.org.common.repositories.ProccessRepository;
import com.manaco.org.common.repositories.TransactionRepository;
import com.manaco.org.common.repositories.UfvRepository;
import com.manaco.org.common.utils.Operator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TransactionService {

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

    public void saveItem(Transaction next) {
        checkUfv(next.getTransactionDetail().getUfv());
        next.getTransactionDetail().setUfv(checkUfv(next.getTransactionDetail().getUfv()));
        if (next.getTransactionDetail().getItem().getQuantity().intValue() < 0) {
            next.getTransactionDetail().getItem().setIsFailure(Boolean.TRUE);
        } else {
            next.getTransactionDetail().getItem().setIsFailure(Boolean.FALSE);
        }
        itemRepository.save(next.getTransactionDetail().getItem());
        transactionRepository.save(next);
        System.out.println("saved successfully with id " + next.getTransactionDetail().getItem().getId());
    }

    private synchronized Ufv checkUfv(Ufv ufv) {
        Ufv actual = ufvRepository.findByCreationDate(ufv.getCreationDate());
        if (actual == null) {
            ufvRepository.save(ufv);
        }
        return actual;
    }

    void saveMoving(Transaction transaction, Item current) {
        Item item = itemRepository.findById(current.getId()).orElse(null);
        if (item != null) {
            MateriaDetail materiaDetail = (MateriaDetail) transaction.getTransactionDetail();
            materiaDetail.setId(0);
            materiaDetail.setItem(item);
            transaction.setTransactionDetail(materiaDetail);
            Ufv actual = ufvRepository.findByCreationDate(transaction.getDate());
            updateItem(item, transaction, actual);
            switch (transaction.getType()) {
                case ENTRY:
                    executeEntry(item, transaction, actual);
                    break;
                case EGRESS:
                    executeEgress(item, transaction, actual);
                    break;
            }
            System.out.println("saved successfully with id " + transaction.getTransactionDetail().getItem().getId());
        } else {
            System.out.println("item not found with  id " + transaction.getTransactionDetail().getItem().getId());
        }
    }

    void saveMoving(Transaction transaction) {
        Item item = itemRepository.findById(transaction.getTransactionDetail().getItem().getId()).orElse(null);
        if (item != null) {
            Ufv actual = ufvRepository.findByCreationDate(transaction.getDate());
            updateItem(item, transaction, actual);
            switch (transaction.getType()) {
                case ENTRY:
                    executeEntry(item, transaction, actual);
                    break;
                case EGRESS:
                    executeEgress(item, transaction, actual);
                    break;
            }
            System.out.println("saved successfully with id " + transaction.getTransactionDetail().getItem().getId());
        } else {
            System.out.println("item not found with  id " + transaction.getTransactionDetail().getItem().getId());
            Item actual = saveItemInit(transaction);
            transaction.getTransactionDetail().setItem(actual);
            saveMoving(transaction);
        }
    }

    private Item updateItem(Item item, Transaction transaction, Ufv actual) {
        if (!item.getLastUpdate().equals(transaction.getDate())) {
            Ufv before = ufvRepository.findByCreationDate(item.getLastUpdate());
            if (item.getQuantity().intValue() != 0) {
                BigDecimal totalNormal = operator.calculateTotal(item.getQuantity(), item.getPrice());
                BigDecimal totalUpdate = operator.calculateUpdate(totalNormal, actual.getValue(), before.getValue());
                BigDecimal newPrice = operator.newPrice(totalUpdate, item.getQuantity());
                BigDecimal ufvValue = operator.caclulateUfvValue(totalUpdate, totalNormal);
                item.setPrice(newPrice);
                item.setLastUpdate(transaction.getDate());
                saveMove(item, TransactionType.UPDATE, ufvValue, actual, transaction.getProcessId());
            }
        }
        return item;
    }

    private void saveMove(Item item, TransactionType type, BigDecimal ufvValue, Ufv ufv, int proccesID) {
        Transaction transaction = new Transaction();
        MateriaDetail detail = new MateriaDetail();
        detail.setUfv(ufv);
        detail.setItem(item);
        transaction.setPriceActual(item.getPrice());
        transaction.setQuantity(item.getQuantity());
        transaction.setType(type);
        transaction.setUfvValue(ufvValue);
        transaction.setDate(item.getLastUpdate());
        transaction.setProcessId(proccesID);
        itemRepository.save(item);
        transactionRepository.save(transaction);
    }

    private void executeEntry(Item item, Transaction transaction, Ufv actual) {
        Transaction entry = new Transaction();
        MateriaDetail detail = (MateriaDetail) transaction.getTransactionDetail();
        entry.setType(TransactionType.ENTRY);
        entry.setPriceNeto(transaction.getPriceNeto());
        BigDecimal quantityTotal = item.getQuantity().add(transaction.getQuantity().setScale(6, BigDecimal.ROUND_CEILING));
        if (quantityTotal.intValue() != 0) {
            BigDecimal totalNormal = operator.calculateTotal(transaction.getQuantity(), transaction.getPriceNeto());
            BigDecimal totalUpdate = operator.calculateTotal(item.getQuantity(), item.getPrice());
            BigDecimal newPrice = operator.newPrice(totalUpdate.add(totalNormal), quantityTotal);
            entry.setPriceActual(newPrice.setScale(6, BigDecimal.ROUND_DOWN));
        } else {
            entry.setPriceActual(item.getPrice());
        }

        entry.setQuantity(transaction.getQuantity().setScale(6, BigDecimal.ROUND_DOWN));
        entry.setDate(transaction.getDate());
        entry.setTransactionDetail(detail);
        entry.setUfvValue(BigDecimal.ZERO);
        entry.setProcessId(transaction.getProcessId());
        item.setPrice(entry.getPriceActual());
        item.setQuantity(quantityTotal);
        detail.setUfv(actual);
        detail.setItem(item);
        itemRepository.save(item);
        transactionRepository.save(entry);
    }

    private void executeEgress(Item item, Transaction transaction, Ufv actual) {
        Transaction egress = new Transaction();
        MateriaDetail detail = (MateriaDetail) transaction.getTransactionDetail();
        egress.setType(TransactionType.EGRESS);
        egress.setPriceActual(item.getPrice());
        egress.setQuantity(transaction.getQuantity());

        egress.setTransactionDetail(detail);
        egress.setDate(transaction.getDate());
        egress.setUfvValue(new BigDecimal(0));
        egress.setProcessId(transaction.getProcessId());
        item.setQuantity(item.getQuantity().subtract(transaction.getQuantity()));
        item.setLastUpdate(transaction.getDate());
        detail.setUfv(actual);
        detail.setItem(item);
        itemRepository.save(item);
        transactionRepository.save(egress);
    }

    public void executeItem(Item actual) {
        Item item = itemRepository.findById(actual.getId()).orElse(null);
        List<Transaction> transactions = transactionRepository.findByTransactionDetailItemItemIdOrderByDateAsc(actual.getId());
        if (item != null && !transactions.isEmpty()) {
            Ufv current = ufvRepository.findByCreationDate(item.getInitialDate());
            Proccess process = proccessRepository.findByNumberProcessAndActiveIn(2, true);
            item.setPrice(actual.getPrice());
            item.setQuantity(transactions.get(0).getQuantity());
            item.setLastUpdate(item.getInitialDate());
            itemRepository.save(item);
            Transaction transaction = new Transaction();
            MateriaDetail detail = new MateriaDetail();

            transaction.setQuantity(item.getQuantity());
            transaction.setPriceActual(item.getPrice());
            transaction.setPriceNeto(BigDecimal.ZERO);
            transaction.setType(TransactionType.INITIAL);
            transaction.setDate(item.getLastUpdate());
            transaction.setUfvValue(BigDecimal.ZERO);
            transaction.setProcessId(process.getId());

            detail.setItem(item);
            detail.setUfv(current);
            transaction.setTransactionDetail(detail);
            transactionRepository.save(transaction);
            transactions.forEach((b) -> {
                if (b.getType() != INITIAL) {
                    b.setProcessId(process.getId());
                    b.setPriceNeto(transaction.getPriceActual());
                    saveMoving(b, item);
                }
            });
        }
    }

    @Transactional
    private synchronized Item saveItemInit(Transaction transaction) {
        Transaction current = new Transaction();
        Item item = new Item();
        item.setId(transaction.getTransactionDetail().getItem().getId());
        item.setPrice(transaction.getPriceNeto());
        item.setQuantity(BigDecimal.ZERO);
        item.setInitialDate(transaction.getDate());
        item.setLastUpdate(transaction.getDate());
        item.setIsFailure(false);
        current.setPriceNeto(item.getPrice());
        current.setPriceActual(item.getPrice());
        current.setDate(transaction.getDate());
        current.setProcessId(transaction.getProcessId());
        current.setQuantity(item.getQuantity());
        current.setUfvValue(BigDecimal.ZERO);
        current.setType(INITIAL);
        itemRepository.save(item);
        transactionRepository.save(current);
        return item;
    }

    public void updateItem(LocalDate localDate) {
        Ufv actual = ufvRepository.findByCreationDate(localDate);
        Proccess proccess = new Proccess();
        proccess.setNumberProcess(3);
        proccess.setType(UPDATE_PROCESS);
        proccessRepository.save(proccess);
        List<Item> items = itemRepository.findAll();
        items.forEach((item) -> {
            updateItem(item, actual, localDate, proccess);
        });
    }

    private void updateItem(Item item, Ufv actual, LocalDate date, Proccess proccess) {
        Ufv before = ufvRepository.findByCreationDate(item.getLastUpdate());
        if (item.getQuantity().intValue() != 0) {
            BigDecimal totalNormal = operator.calculateTotal(item.getQuantity(), item.getPrice());
            BigDecimal totalUpdate = operator.calculateUpdate(totalNormal, actual.getValue(), before.getValue());
            BigDecimal newPrice = operator.newPrice(totalUpdate, item.getQuantity());
            BigDecimal ufvValue = operator.caclulateUfvValue(totalUpdate, totalNormal);
            item.setPrice(newPrice.setScale(6, BigDecimal.ROUND_DOWN));
            item.setLastUpdate(date);
            saveMove(item, TransactionType.UPDATE, ufvValue, actual, proccess.getId());
        } else {
            saveMove(item, TransactionType.UPDATE, BigDecimal.ZERO, actual, proccess.getId());
        }
    }

}
