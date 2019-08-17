package com.manaco.org.repositories;

import com.manaco.org.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository extends MongoRepository<Stock, Long> {

//    Stock findByItemId(String id);
}
