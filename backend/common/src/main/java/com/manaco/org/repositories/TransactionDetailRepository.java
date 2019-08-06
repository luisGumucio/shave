package com.manaco.org.repositories;

import com.manaco.org.model.TransactionDetail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailRepository extends MongoRepository<TransactionDetail, ObjectId> {
}
