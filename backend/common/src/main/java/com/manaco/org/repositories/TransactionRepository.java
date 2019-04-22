package com.manaco.org.repositories;

import com.manaco.org.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByTransactionDetailItemIdOrderByDateAsc(Pageable pageable, Long id);
    List<Transaction> findByTransactionDetailItemIdOrderByDateAsc(String id);
}

