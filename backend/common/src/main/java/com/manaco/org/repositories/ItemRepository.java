package com.manaco.org.repositories;

import com.manaco.org.dto.ItemDto;
import com.manaco.org.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemRepository extends MongoRepository<Item, String> {

    Page<Item> findByIdentifier(Pageable pageable, String identifier);
    List<Item> findByIdentifier(String identifier);

    List<Item> findAllByIdentifier(String identifier);
}