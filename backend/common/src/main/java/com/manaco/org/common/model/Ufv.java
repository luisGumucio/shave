package com.manaco.org.common.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Ufv {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Digits(integer = 1, fraction = 5)
    @NotNull
    @Column(precision = 6, scale = 5)
    private BigDecimal value;

    @NotNull
    private LocalDate creationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
