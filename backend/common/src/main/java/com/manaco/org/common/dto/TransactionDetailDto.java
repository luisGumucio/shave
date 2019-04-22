package com.manaco.org.common.dto;

public class TransactionDetailDto {

    private int id;
    private String name;
    private String itemId;

    public TransactionDetailDto(int id, String name, String itemId) {
        this.id = id;
        this.name = name;
        this.itemId = itemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
