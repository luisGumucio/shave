package com.manaco.org.dto;

public class TransactionDto {

    private Long id;

    public TransactionDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
