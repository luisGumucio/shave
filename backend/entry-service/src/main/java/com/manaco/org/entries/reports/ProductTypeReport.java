package com.manaco.org.entries.reports;

import java.math.BigDecimal;

public class ProductTypeReport {

    private String type;
    private String moveType;
    private BigDecimal entry;
    private BigDecimal egress;
    private BigDecimal totalEntry;
    private BigDecimal totalEgress;
    private BigDecimal update;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoveType() {
        return moveType;
    }

    public void setMoveType(String moveType) {
        this.moveType = moveType;
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

    public BigDecimal getUpdate() {
        return update;
    }

    public void setUpdate(BigDecimal update) {
        this.update = update;
    }
}
