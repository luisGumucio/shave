package com.manaco.org.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "item")
public class Item {

    @Id
    private String id;
    @NotNull
    @Column(precision = 19, scale = 6)
    private BigDecimal quantity;
    @NotNull
    @Column(precision = 19, scale = 6)
    private BigDecimal price;
    private LocalDate initialDate;
    private LocalDate lastUpdate;
    private Boolean isFailure;

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
}
