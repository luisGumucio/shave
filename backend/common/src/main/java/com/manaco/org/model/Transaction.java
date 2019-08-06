package com.manaco.org.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction implements Comparable<Transaction> {

    @Id
    private ObjectId id;
    private TransactionType type;
    private BigDecimal quantity;
    private BigDecimal priceNeto;
    private BigDecimal priceActual;

    private BigDecimal ufvValue;

    @NotNull
    private LocalDate transactionDate;

    private String processId;

    private String itemId;

    @DBRef
    private TransactionDetail detail;

    @Transient
    private Item item;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceNeto() {
        return priceNeto;
    }

    public void setPriceNeto(BigDecimal priceNeto) {
        this.priceNeto = priceNeto;
    }

    public BigDecimal getPriceActual() {
        return priceActual;
    }

    public void setPriceActual(BigDecimal priceActual) {
        this.priceActual = priceActual;
    }

    public BigDecimal getUfvValue() {
        return ufvValue;
    }

    public void setUfvValue(BigDecimal ufvValue) {
        this.ufvValue = ufvValue;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public TransactionDetail getDetail() {
        return detail;
    }

    public void setDetail(TransactionDetail detail) {
        this.detail = detail;
    }

    @Override
    public int compareTo(Transaction o) {
        return this.getItem().getId().compareTo(o.getItem().getId());
    }
}
