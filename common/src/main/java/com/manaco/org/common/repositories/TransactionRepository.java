package com.manaco.org.common.repositories;

import com.manaco.org.common.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionDetailItemItemIdOrderByDateAsc(Long id);
}

