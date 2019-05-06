package com.manaco.org.dto;

import java.math.BigDecimal;

public class ItemDto {

    private Long totalItem;
    private BigDecimal total;
    private BigDecimal totalQuantity;

    public ItemDto(Long totalItem, BigDecimal total, BigDecimal totalQuantity) {
        this.totalItem = totalItem;
        this.total = total;
        this.totalQuantity = totalQuantity;
    }

    public Long getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Long totalItem) {
        this.totalItem = totalItem;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
