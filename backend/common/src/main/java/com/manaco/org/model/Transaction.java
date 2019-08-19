package com.manaco.org.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {


    @Id
    private String id;
    private TransactionType type;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate transactionDate;
    private BigDecimal entry;
    private BigDecimal egress;
    private BigDecimal balance;
    private BigDecimal priceNeto;
    private BigDecimal priceActual;
    @DBRef
    private Ufv ufv;
    private BigDecimal totalEntry;
    private BigDecimal totalEgress;
    private BigDecimal totalNormal;
    private BigDecimal totalUpdate;
    private BigDecimal increment;

    private String processId;

    @DBRef
    private TransactionDetail detail;

    @DBRef
    private Item item;

    @Indexed(name = "identifier_index", direction = IndexDirection.DESCENDING)
    private TransactionOption identifier;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getEntry() {
        return entry;
    }

    public void setEntry(BigDecimal entry) {
        this.entry = entry;
    }

    public BigDecimal getEgress() {
        return egress;
    }

    public void setEgress(BigDecimal egress) {
        this.egress = egress;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public Ufv getUfv() {
        return ufv;
    }

    public void setUfv(Ufv ufv) {
        this.ufv = ufv;
    }

    public BigDecimal getTotalEntry() {
        return totalEntry;
    }

    public void setTotalEntry(BigDecimal totalEntry) {
        this.totalEntry = totalEntry;
    }

    public BigDecimal getTotalEgress() {
        return totalEgress;
    }

    public void setTotalEgress(BigDecimal totalEgress) {
        this.totalEgress = totalEgress;
    }

    public BigDecimal getTotalNormal() {
        return totalNormal;
    }

    public void setTotalNormal(BigDecimal totalNormal) {
        this.totalNormal = totalNormal;
    }

    public BigDecimal getTotalUpdate() {
        return totalUpdate;
    }

    public void setTotalUpdate(BigDecimal totalUpdate) {
        this.totalUpdate = totalUpdate;
    }

    public BigDecimal getIncrement() {
        return increment;
    }

    public void setIncrement(BigDecimal increment) {
        this.increment = increment;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public TransactionDetail getDetail() {
        return detail;
    }

    public void setDetail(TransactionDetail detail) {
        this.detail = detail;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public TransactionOption getIdentifier() {
        return identifier;
    }

    public void setIdentifier(TransactionOption identifier) {
        this.identifier = identifier;
    }

    //    @Override
//    public int compareTo(Transaction o) {
//        return this.getItem().getId().compareTo(o.getItem().getId());
//    }
}
