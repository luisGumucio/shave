package com.manaco.org.entries;

import com.manaco.org.entries.reports.TotalItemReport;
import com.manaco.org.entries.reports.TotalItemSumReport;
import com.manaco.org.model.Item;
import com.manaco.org.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.Document;

import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping(path = "/{id}")
    public Item getById(@PathVariable String id) {
        return itemRepository.findById(id).orElse(null);
    }

    @GetMapping
    public Page<Item> get(@RequestParam int page, @RequestParam String identifier) {
        System.out.println(page);

        return itemRepository.findByIdentifier(PageRequest.of(page, 4), identifier);
    }

    @GetMapping(path = "/itemtotal")
    public List<TotalItemReport> getItemTotal(@RequestParam String identifier) {
        Aggregation agg = newAggregation(
                match(Criteria.where("identifier").is(identifier)),
                project("identifier", "price", "quantity").and("price").multiply("quantity")
                        .as("result"),
                group("identifier").sum("result").as("total"));
        AggregationResults<TotalItemReport> groupResults
                = mongoTemplate.aggregate(agg, Item.class, TotalItemReport.class);
        List<TotalItemReport> result = groupResults.getMappedResults();
        return result;
    }

    @GetMapping(path = "/itemsum")
    public List<TotalItemReport> getItemSum(@RequestParam String identifier) {
        Aggregation agg = newAggregation(
                match(Criteria.where("identifier").is(identifier)),
                group("identifier").count().as("total"),
                project("total").and("identifier").previousOperation(),
                sort(Sort.Direction.DESC, "total")

        );

        //Convert the aggregation result into a List
        AggregationResults<TotalItemReport> groupResults
                = mongoTemplate.aggregate(agg, Item.class, TotalItemReport.class);
        List<TotalItemReport> result = groupResults.getMappedResults();

        return result;
    }
}
