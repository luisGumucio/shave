package com.manaco.org.entries;

import com.manaco.org.dto.TransactionDto;
import com.manaco.org.model.Transaction;
import com.manaco.org.repositories.TransactionRepository;
import com.manaco.org.repositories.UfvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UfvRepository ufvRepository;

    public List<TransactionDto> getTransactionDto(int page, String identifier) {
        Page<Transaction> transactionList = transactionRepository.findByIdentifier(PageRequest.of(page, 10), identifier);
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Transaction transaction: transactionList.getContent()) {
            TransactionDto dto = new TransactionDto();
            switch (transaction.getType()) {
                case INITIAL:
//                     dto = buildInitial(transaction);
                     break;
            }
            transactionDtos.add(dto);
        }
        return transactionDtos;
    }

    public List<TransactionDto> getTransactionDtoItem(int page, String id) {
        Page<Transaction> transactionList = transactionRepository.findByItemId(PageRequest.of(page, 10), id);
        List<TransactionDto> transactionDtos = new ArrayList<>();
        for (Transaction transaction: transactionList.getContent()) {
            TransactionDto dto = new TransactionDto();
            switch (transaction.getType()) {
                case INITIAL:
//                    dto = buildInitial(transaction);
                    break;
                case EGRESS:
//                    dto = buildEgress(transaction);
            }
            transactionDtos.add(dto);
        }
        return transactionDtos;
    }

//    private TransactionDto buildInitial(Transaction transaction) {
//        TransactionDto dto = new TransactionDto();
//        dto.setType(transaction.getType());
//        dto.setDate(transaction.getTransactionDate());
//        dto.setEntry(BigDecimal.ZERO);
//        dto.setEgress(BigDecimal.ZERO);
//        dto.setQuantity(transaction.getQuantity());
//        dto.setPriceNeto(BigDecimal.ZERO);
//        dto.setPriceActual(transaction.getPriceActual());
//        dto.setUfv(getUfv(transaction.getTransactionDate()));
//        dto.setTotalEgress(BigDecimal.ZERO);
//        dto.setTotalEntry(BigDecimal.ZERO);
//        dto.setTotalNormal(transaction.getPriceActual().multiply(transaction.getQuantity()));
//        dto.setTotalUpdate(transaction.getPriceActual().multiply(transaction.getQuantity()));
//        dto.setIncrement(BigDecimal.ZERO);
//        return dto;
//    }
//
//    private TransactionDto buildEntry(Transaction transaction) {
//        TransactionDto dto = new TransactionDto();
//        dto.setType(transaction.getType());
//        dto.setDate(transaction.getTransactionDate());
//        dto.setEntry(transaction.getQuantity());
//        dto.setEgress(BigDecimal.ZERO);
//        dto.setQuantity(transaction.getQuantity());
//        dto.setPriceNeto(BigDecimal.ZERO);
//        dto.setPriceActual(transaction.getPriceActual());
//        dto.setUfv(getUfv(transaction.getTransactionDate()));
//        dto.setTotalEgress(BigDecimal.ZERO);
//        dto.setTotalEntry(BigDecimal.ZERO);
//        dto.setTotalNormal(transaction.getPriceActual().multiply(transaction.getQuantity()));
//        dto.setTotalUpdate(transaction.getPriceActual().multiply(transaction.getQuantity()));
//        dto.setIncrement(BigDecimal.ZERO);
//        return dto;
//    }
//
//    private TransactionDto buildEgress(Transaction transaction) {
//        TransactionDto dto = new TransactionDto();
//        dto.setType(transaction.getType());
//        dto.setDate(transaction.getTransactionDate());
//        dto.setEntry(BigDecimal.ZERO);
//        dto.setEgress(transaction.getQuantity());
//        dto.setQuantity(transaction.getSaldo());
//        dto.setPriceNeto(BigDecimal.ZERO);
//        dto.setPriceActual(transaction.getPriceActual());
//        dto.setUfv(getUfv(transaction.getTransactionDate()));
//        dto.setTotalEgress(transaction.getSaldo().multiply(transaction.getQuantity()));
//        dto.setTotalEntry(BigDecimal.ZERO);
//        dto.setTotalNormal(transaction.getPriceActual().multiply(transaction.getQuantity()));
//        dto.setTotalUpdate(transaction.getPriceActual().multiply(transaction.getQuantity()));
//        dto.setIncrement(BigDecimal.ZERO);
//        return dto;
//    }
//
//    private BigDecimal getUfv(LocalDate transactionDate) {
//        return ufvRepository.findByCreationDate(transactionDate).getValue();
//    }
}
