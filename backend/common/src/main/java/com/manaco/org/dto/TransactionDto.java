package com.manaco.org.dto;

import com.manaco.org.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDto {

    private TransactionType type;
    private LocalDate date;
    private BigDecimal entry;
    private BigDecimal egress;
    private BigDecimal quantity;
    private BigDecimal priceNeto;
    private BigDecimal priceActual;
    private BigDecimal ufv;
    private BigDecimal totalEntry;
    private BigDecimal totalEgress;
    private BigDecimal totalNormal;
    private BigDecimal totalUpdate;
    private BigDecimal increment;

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public BigDecimal getUfv() {
        return ufv;
    }

    public void setUfv(BigDecimal ufv) {
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
}
