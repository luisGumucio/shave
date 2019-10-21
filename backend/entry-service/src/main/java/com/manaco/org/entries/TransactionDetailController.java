package com.manaco.org.entries;

import com.manaco.org.model.Stock;
import com.manaco.org.model.TransactionDetail;
import com.manaco.org.repositories.TransactionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/detail")
@CrossOrigin(origins = "*")
public class TransactionDetailController {

    @Autowired
    private TransactionDetailRepository detailRepository;

    @GetMapping
    public List<TransactionDetail> get() {
        return detailRepository.findAllInformationAlmacen("24406");
    }
}
