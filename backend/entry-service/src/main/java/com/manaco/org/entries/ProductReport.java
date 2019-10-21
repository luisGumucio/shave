package com.manaco.org.entries;

import com.manaco.org.entries.excel.ExcelGenerator;
import com.manaco.org.model.Transaction;
import com.manaco.org.model.TransactionOption;
import com.manaco.org.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ProductReport {


    @Autowired
    TransactionRepository transactionRepository;

    public ResponseEntity<InputStreamResource> downloadProductGeneral(String identifier) throws IOException {
        List<Transaction> transactions = transactionRepository.findAllByIdentifierAndType("PRODUCTO",
                "G_INITIAL");

        ByteArrayInputStream in = null;

        in = ExcelGenerator.downloadProducFinish(transactions);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    public ResponseEntity<InputStreamResource> downloadProductByTienda(String identifier, String tienda) {
//        List<Transaction> transactions = transactionRepository.findAllByIdentifierAndItemId(
//                "PRODUCTO", "00014100");
        List<Transaction> transactions = transactionRepository.findAllInformationAlmacen(tienda);


        ByteArrayInputStream in = null;

        in = ExcelGenerator.downloadProducFinish(transactions);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }
}
