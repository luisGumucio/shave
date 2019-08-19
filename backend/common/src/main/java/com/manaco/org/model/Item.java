package com.manaco.org.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Document
public class Item {

    @Id
    private String id;
    @NotNull
    private BigDecimal quantity;
    @NotNull
    private BigDecimal price;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate initialDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate lastUpdate;
    private BigDecimal total;
    private Boolean isFailure;

    @Indexed(name = "identifier_index", direction = IndexDirection.DESCENDING)
    private TransactionOption identifier;

    public Item() {
    }

    public Item(String itemId, LocalDate date) {
        this.id = itemId;
        quantity = BigDecimal.ZERO;
        price = BigDecimal.ZERO;
        lastUpdate = date;
        initialDate = date;
        isFailure = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDate initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Boolean getIsFailure() {
        return isFailure;
    }

    public void setIsFailure(Boolean isFailure) {
        this.isFailure = isFailure;
    }

    public TransactionOption getIdentifier() {
        return identifier;
    }

    public void setIdentifier(TransactionOption identifier) {
        this.identifier = identifier;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
