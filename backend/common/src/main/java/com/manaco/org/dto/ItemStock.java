package com.manaco.org.dto;

import com.manaco.org.model.Item;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;

public class ItemStock {

    @DBRef
    private Item item;

    private BigDecimal quantity;
    private BigDecimal total;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
