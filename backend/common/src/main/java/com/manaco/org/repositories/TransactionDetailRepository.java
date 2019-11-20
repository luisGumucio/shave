package com.manaco.org.repositories;

import com.manaco.org.model.TransactionDetail;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDetailRepository extends MongoRepository<TransactionDetail, ObjectId> {

    @Query(value = "{'information.Almacen' : ?0}")
    List<TransactionDetail> findAllInformationAlmacen(String Almacen);
}
