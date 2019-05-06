package com.manaco.org.repositories;

import com.manaco.org.dto.ItemDto;
import com.manaco.org.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
public interface ItemRepository extends JpaRepository<Item, String> {
//    Page<Item> findAllByOrderByItemIdAsc(PageRequest pageRequest);
    @Query(value = "SELECT new com.manaco.org.dto.ItemDto(COUNT(i.itemId), SUM(i.price * i.quantity), SUM(i.quantity)) FROM Item i")
    ItemDto fetchItemInformationDtoSum();
}
