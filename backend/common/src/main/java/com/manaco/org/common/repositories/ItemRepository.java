package com.manaco.org.common.repositories;

import com.manaco.org.common.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface ItemRepository extends JpaRepository<Item, String> {
//    Page<Item> findAllByOrderByItemIdAsc(PageRequest pageRequest);
}
