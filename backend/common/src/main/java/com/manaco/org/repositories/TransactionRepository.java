package com.manaco.org.repositories;

import com.manaco.org.model.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, ObjectId> {
//    Page<Transaction> findByTransactionDetailItemItemIdOrderByDateAsc(Pageable pageable, String id);
//    List<Transaction> findByTransactionDetailItemItemIdOrderByDateAsc(String id);
}

