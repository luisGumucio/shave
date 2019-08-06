package com.manaco.org.repositories;

import com.manaco.org.dto.ItemDto;
import com.manaco.org.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface ItemRepository extends MongoRepository<Item, String> {
//    Page<Item> findAllByOrderByItemIdAsc(PageRequest pageRequest);
//    @Query(value = "SELECT new com.manaco.org.dto.ItemDto(COUNT(i.id), SUM(i.price * i.quantity), SUM(i.quantity)) FROM Item i")
//    ItemDto fetchItemInformationDtoSum();


    Page<Item> findByIdentifier(Pageable pageable, String identifier);
}
