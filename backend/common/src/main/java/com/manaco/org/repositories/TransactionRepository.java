package com.manaco.org.repositories;

import com.manaco.org.model.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
//    Page<Transaction> findByTransactionDetailItemItemIdOrderByDateAsc(Pageable pageable, String id);
//    List<Transaction> findByTransactionDetailItemItemIdOrderByDateAsc(String id);

    Page<Transaction> findByIdentifier(Pageable pageable, String identifier);

    Page<Transaction> findByItemId(Pageable pageable, String id);

    Page<Transaction> findByItemIdAndTransactionDate(Pageable pageable, String id, LocalDate creationDate);

    Page<Transaction>  findByTypeAndIdentifier (Pageable pageable, String type, String identifier);

}

