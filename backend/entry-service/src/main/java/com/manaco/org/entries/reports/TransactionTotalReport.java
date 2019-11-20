package com.manaco.org.entries.reports;

import java.math.BigDecimal;

public class TransactionTotalReport {

    private BigDecimal totalNormal;
    private BigDecimal totalUpdate;
    private BigDecimal totalIncrement;

    private String name;

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

    public BigDecimal getTotalIncrement() {
        return totalIncrement;
    }

    public void setTotalIncrement(BigDecimal totalIncrement) {
        this.totalIncrement = totalIncrement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
