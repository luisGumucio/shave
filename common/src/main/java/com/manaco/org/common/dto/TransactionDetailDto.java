package com.manaco.org.common.dto;

public class TransactionDetailDto {

    private int id;
    private String name;
    private long itemId;

    public TransactionDetailDto(int id, String name, long itemId) {
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

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
}
