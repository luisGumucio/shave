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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductReport {


    @Autowired
    TransactionRepository transactionRepository;

    public ResponseEntity<InputStreamResource> downloadProductGeneral(String identifier) throws IOException {
        List<Transaction> transactions = transactionRepository.findAllByIdentifierAndTypeStartsWith("PRODUCTO",
                "G_");

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

    public ResponseEntity<InputStreamResource> downloadProductByItem(ProductOption option) {
        List<Transaction> transactions = transactionRepository.findAllByIdentifierAndTypeStartsWithAndItemId("PRODUCTO",
                "G_", option.getTienda());

        List<ProductDetail> productDetails = new ArrayList<>();
        for (Transaction transaction: transactions) {
            if(transaction.getType().name().equals("G_INITIAL")) {
                ProductDetail detail = new ProductDetail();
                detail.setType(transaction.getType().name());
                detail.setQuantity(transaction.getBalance());
                detail.setDetalle("INICIAL");
                productDetails.add(detail);
            } else if(transaction.getType().name().equals("G_ENTRY")) {
                ProductDetail detail = null;
                detail = productDetails.stream().filter(b ->
                        b.getDetalle().equals(transaction.getInformation().get("TIPO_MOV"))).findFirst().orElse(null);
                if(detail  == null) {
                    detail = new ProductDetail();
                    detail.setType("PARES_DEBER");
                    detail.setQuantity(transaction.getEntry());
                    detail.setDetalle(transaction.getInformation().get("TIPO_MOV"));
                    productDetails.add(detail);
                } else {
                    productDetails.removeIf(b -> b.getDetalle().equals(transaction.getInformation().get("TIPO_MOV")));
                    detail.setQuantity(detail.getQuantity().add(transaction.getEntry()));
                    productDetails.add(detail);
                }
            } else if (transaction.getType().name().equals("G_EGRESS")) {
                ProductDetail detail = null;
                detail = productDetails.stream().filter(b ->
                        b.getDetalle().equals(transaction.getInformation().get("TIPO_MOV"))).findFirst().orElse(null);
                if(detail  == null) {
                    detail = new ProductDetail();
                    detail.setType("PARES_HABER");
                    detail.setQuantity(transaction.getEgress());
                    detail.setDetalle(transaction.getInformation().get("TIPO_MOV"));
                    productDetails.add(detail);
                } else {
                    productDetails.removeIf(b -> b.getDetalle().equals(transaction.getInformation().get("TIPO_MOV")));
                    detail.setQuantity(detail.getQuantity().add(transaction.getEgress()));
                    productDetails.add(detail);
                }
            }
        }

        ByteArrayInputStream in = null;

        in = ExcelGenerator.downloadDetail(productDetails);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    public ResponseEntity<InputStreamResource> downloadProductByType(ProductOption option) {
        List<Transaction> transactions = transactionRepository.findAllByIdentifierAndTypeStartsWithAndItemId("PRODUCTO",
                "G_", option.getTienda());
        return null;
    }
}
