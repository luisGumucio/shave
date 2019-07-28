package com.manaco.org.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
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
    private LocalDate transactionDate;

    @ManyToOne
    @JoinColumn
    private Process transactions;

    private String itemId;

    @Transient
    private Item item;

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

    public Process getTransactions() {
        return transactions;
    }

    public void setTransactions(Process transactions) {
        this.transactions = transactions;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public int compareTo(Transaction o) {
        return this.getItem().getId().compareTo(o.getItem().getId());
    }
}
