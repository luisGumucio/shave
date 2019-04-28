package com.manaco.org.repositories;

import com.manaco.org.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface ItemRepository extends JpaRepository<Item, String> {
//    Page<Item> findAllByOrderByItemIdAsc(PageRequest pageRequest);
}
