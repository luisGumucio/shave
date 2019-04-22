package com.manaco.org.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Transaction implements Comparable<Transaction> {

    @Id
    @GeneratedValue
    private long id;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Column(precision = 19, scale = 6)
    private BigDecimal quantity;
    @Column(precision = 19, scale = 6)
    private BigDecimal priceNeto;
    @Column(precision = 19, scale = 6)
    private BigDecimal priceActual;
    @Column(precision = 19, scale = 6)
    private BigDecimal ufvValue;
    @NotNull
    private LocalDate date;
    private int processId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transactionDetail_id", referencedColumnName = "id")
    private TransactionDetail transactionDetail;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionDetail getTransactionDetail() {
        return transactionDetail;
    }

    public void setTransactionDetail(TransactionDetail transactionDetail) {
        this.transactionDetail = transactionDetail;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }
    
    @Override
    public int compareTo(Transaction o) {
        return this.getTransactionDetail().getItem().getId().compareTo(o.getTransactionDetail().getItem().getId());
    }
}
