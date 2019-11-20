package com.manaco.org.repositories;

import com.manaco.org.model.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {


    Page<Transaction> findByIdentifier(Pageable pageable, String identifier);

    List<Transaction> findAllByIdentifier(String identifier);

    Page<Transaction> findByItemId(Pageable pageable, String id);

    List<Transaction> findByItemId(String id);

    Page<Transaction> findByItemIdAndTransactionDate(Pageable pageable, String id, LocalDate creationDate);

    List<Transaction> findByTransactionDateAndIdentifierAndType(LocalDate creationDate, String identifier,
                                                                String type);

    Page<Transaction> findByTransactionDateBetweenAndIdentifier(Pageable pageable, LocalDate initDate, LocalDate endDate,
                                                                String identifier);

    Page<Transaction>  findByTypeAndIdentifier (Pageable pageable, String type, String identifier);

    Page<Transaction> findByItemIdAndTransactionDateBetween(PageRequest of, String id, LocalDate initDate, LocalDate endDate);

    List<Transaction> findAllByIdentifierAndTypeStartsWith(String producto, String type);

    Optional<Transaction> findByItemIdAndType(String itemId, String type);

    @Query(value = "{'information.Almacen' : ?0}")
    List<Transaction> findAllInformationAlmacen(String Almacen);

    List<Transaction> findAllByIdentifierAndTypeStartsWithAndItemId(String identifier, String type, String itemId);
}

