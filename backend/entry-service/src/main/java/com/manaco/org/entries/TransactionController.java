package com.manaco.org.entries;

import com.manaco.org.dto.FilterDate;
import com.manaco.org.dto.TransactionDto;
import com.manaco.org.entries.reports.TotalItemReport;
import com.manaco.org.entries.reports.TransactionTotalReport;
import com.manaco.org.model.Transaction;
import com.manaco.org.model.TransactionDetail;
import com.manaco.org.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class TransactionController {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private TransactionRepository transactionRepository;



    @GetMapping(path = "/identifier/{identifier}")
    public Page<Transaction> get(@RequestParam(defaultValue = "0") int page, @PathVariable String identifier) {
        return transactionRepository.findByIdentifier(PageRequest.of(page, 10), identifier);
    }

    @GetMapping(path = "/{id}")
    public Page<Transaction> get(@PathVariable String id, @RequestParam(defaultValue = "0") int page) {
        return transactionRepository.findByItemId(PageRequest.of(page, 20), id);
    }


    //metodo para busqueda por fecha
    @PostMapping(path = "/reportTransaction/{id}")
    public Page<Transaction> getType1(@RequestParam(defaultValue = "0") int page,
                                      @RequestBody FilterDate filterDate,
                                      @PathVariable String id) {

        if (filterDate.getLastDate() == null) {
            return transactionRepository.findByItemIdAndTransactionDate(PageRequest.of(page, 20),
                    id, filterDate.getInitDate());
        } else {
            return null;
//            return transactionRepository.findByTransactionDateBetween(PageRequest.of(page, 10), filterDate.getInitDate(), filterDate.getLastDate());
        }
    }

    //metodo para busqueda por fecha general
    @PostMapping(path = "/transactionDate")
    public List<Transaction> getType1(@RequestBody FilterDate filterDate) {

        return transactionRepository.findByTransactionDateAndIdentifierAndType(
                filterDate.getInitDate(), filterDate.getIdentifier(), filterDate.getType());
//        if (filterDate.getLastDate() == null) {
//            return transactionRepository.findByTransactionDateAndIdentifier(PageRequest.of(page, 20),
//                    filterDate.getInitDate(), filterDate.getIdentifier());
//        } else {
//            return transactionRepository
//                    .findByTransactionDateBetweenAndIdentifier(
//                            PageRequest.of(page, 10), filterDate.getInitDate(),
//                            filterDate.getLastDate(), filterDate.getIdentifier());
//        }
    }

    @PostMapping(path = "/reportDateTransaction")
    public List<TransactionTotalReport> getTransactionTotal(@RequestBody FilterDate filterDate) {

        Aggregation aggregation = newAggregation(
                match(Criteria.where("identifier").is(filterDate.getIdentifier())
                        .and("transactionDate").is(filterDate.getInitDate())),
                group("identifier")
                        .sum("totalNormal").as("totalNormal")
                        .sum("totalUpdate").as("totalUpdate")
                        .sum("increment").as("totalIncrement"),
                sort(Sort.Direction.ASC, previousOperation(), "identifier"));

        AggregationResults<TransactionTotalReport> groupResults = mongoTemplate.aggregate(
                aggregation, Transaction.class, TransactionTotalReport.class);

        List<TransactionTotalReport> salesReport = groupResults.getMappedResults();

        return salesReport;
    }

    @PostMapping(path = "/reportDeber")
    public List<TransactionTotalReport> getReportDeber(@RequestBody FilterDate filterDate) {

        Aggregation aggregation = newAggregation(
                match(Criteria.where("identifier").is(filterDate.getIdentifier())),
//                        .and("type").is("G_EGRESS")),
                group("identifier")
                        .sum("totalNormal").as("totalNormal")
                        .sum("balance").as("totalUpdate"),
                project("name"),
//                        .sum("increment").as("totalIncrement"),
                sort(Sort.Direction.ASC, previousOperation(), "identifier"));

        AggregationResults<TransactionTotalReport> groupResults = mongoTemplate.aggregate(
                aggregation, Transaction.class, TransactionTotalReport.class);

        List<TransactionTotalReport> salesReport = groupResults.getMappedResults();

        return salesReport;
    }


    @GetMapping(path = "/transactionTotal")
    public List<TotalItemReport> getItemTotal(@RequestParam String id) {
        Aggregation agg = newAggregation(match(Criteria.where("item").is(id)),
                group("item")
                        .sum("increment").as("total"),
                sort(Sort.Direction.ASC, previousOperation(), "item"));
        AggregationResults<TotalItemReport> groupResults
                = mongoTemplate.aggregate(agg, Transaction.class, TotalItemReport.class);

        List<TotalItemReport> result = groupResults.getMappedResults();
        return result;
    }

    @GetMapping(path = "reportTransactionTotal/{identifier}")
    public List<TransactionTotalReport> getTransactionTotal(@PathVariable String identifier) {

        Aggregation aggregation = newAggregation(
                match(Criteria.where("identifier").is(identifier)),
                group("identifier")
                        .sum("totalNormal").as("totalNormal")
                        .sum("totalUpdate").as("totalUpdate")
                        .sum("increment").as("totalIncrement"),
                sort(Sort.Direction.ASC, previousOperation(), "identifier"));

        AggregationResults<TransactionTotalReport> groupResults = mongoTemplate.aggregate(
                aggregation, Transaction.class, TransactionTotalReport.class);

        List<TransactionTotalReport> salesReport = groupResults.getMappedResults();

        return salesReport;
    }


}
