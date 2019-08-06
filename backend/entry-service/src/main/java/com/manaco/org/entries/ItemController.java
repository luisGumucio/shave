package com.manaco.org.entries;

import com.manaco.org.model.Item;
import com.manaco.org.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping(path = "/{id}")
    public Item getById(@PathVariable String id) {
        return itemRepository.findById(id).get();
    }

    @GetMapping
    public Page<Item> get(@RequestParam(defaultValue = "0") int page, @RequestParam String identifier) {
        return itemRepository.findByIdentifier(new PageRequest(page, 4), identifier);
    }
}
