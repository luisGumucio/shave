package com.manaco.org.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

@Document
public class TransactionDetail implements Serializable {

    @Id
    private ObjectId id;
    private Map<String, String> information;

    public TransactionDetail() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Map<String, String> getInformation() {
        return information;
    }

    public void setInformation(Map<String, String> information) {
        this.information = information;
    }
}
