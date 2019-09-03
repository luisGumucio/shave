package com.manaco.org.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manaco.org.dto.ItemStock;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Document
public class Stock {

    @Id
    private long id;

    private String name;

    private List<ItemStock> items;

    public Stock() {
        items = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemStock> getItems() {
        return items;
    }

    public void setItems(List<ItemStock> items) {
        this.items = items;
    }

    public void addItem(ItemStock item) {
        this.items.add(item);
    }

}
