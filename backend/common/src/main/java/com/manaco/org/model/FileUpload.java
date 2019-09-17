package com.manaco.org.model;

import java.util.Date;

public class FileUpload {

    private String id;
    private String name;
    private Date creationTime;
    private TransactionOption option;
    private int totalRows;

    public FileUpload(String name, TransactionOption option) {
        this.name = name;
        this.option = option;
        creationTime = new Date();
    }

    public FileUpload() {
        creationTime = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public TransactionOption getOption() {
        return option;
    }

    public void setOption(TransactionOption option) {
        this.option = option;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }
}
