package com.manaco.org.entries;

import com.manaco.org.model.ItemStock;
import com.manaco.org.model.Stock;
import com.manaco.org.repositories.ItemStockRepository;
import com.manaco.org.repositories.StockRepository;
import com.manaco.org.repositories.UfvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@CrossOrigin(origins = "*")
public class StockController {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ItemStockRepository itemStockRepository;

    @GetMapping
    public Page<Stock> get(@RequestParam(defaultValue = "0") int page) {
        return stockRepository.findAll(new PageRequest(page, 10));
    }

//    @GetMapping(path = "/{id}")
//    public Stock getByItem(@RequestParam(defaultValue = "0") int page, @PathVariable String id) {
//        return stockRepository.findByItemId(id);
//    }

    @GetMapping(path = "/byItem/{id}")
    public List<ItemStock> getByItem(@PathVariable String id) {
        return itemStockRepository.findByItemId(id);
    }

}