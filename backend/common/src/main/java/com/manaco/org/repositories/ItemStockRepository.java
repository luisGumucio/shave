package com.manaco.org.repositories;

import com.manaco.org.model.ItemStock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ItemStockRepository extends MongoRepository<ItemStock, String> {

    Optional<ItemStock> findByStockIdAndItemId(long stockId, String itemId);
}
