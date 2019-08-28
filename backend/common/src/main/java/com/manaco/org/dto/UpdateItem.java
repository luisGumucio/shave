package com.manaco.org.dto;

import com.manaco.org.model.TransactionOption;

import java.time.LocalDate;

public class UpdateItem {

    private LocalDate date;
    private TransactionOption option;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionOption getOption() {
        return option;
    }

    public void setOption(TransactionOption option) {
        this.option = option;
    }
}
