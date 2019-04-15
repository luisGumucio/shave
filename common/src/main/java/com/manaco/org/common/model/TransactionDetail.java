package com.manaco.org.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.manaco.org.common.utils.TransactionDeserializer;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@JsonDeserialize(using = TransactionDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class TransactionDetail implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Ufv ufv;

    @ManyToOne
    private Item item;

    @OneToOne(mappedBy = "transactionDetail")
    private Transaction transaction;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ufv getUfv() {
        return ufv;
    }

    public void setUfv(Ufv ufv) {
        this.ufv = ufv;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
