package com.manaco.org.dto;

import java.time.LocalDate;

public class FilterDate {

    private LocalDate initDate;
    private LocalDate lastDate;

    public LocalDate getInitDate() {
        return initDate;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public LocalDate getLastDate() {
        return lastDate;
    }

    public void setLastDate(LocalDate lastDate) {
        this.lastDate = lastDate;
    }
}
