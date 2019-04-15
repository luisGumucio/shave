package com.manaco.org.common.model;

import java.util.Date;

public class FileUpload {

    private String name;
    private Date creationTime;
    private TransactionOption option;

    public FileUpload(String name, TransactionOption option) {
        this.name = name;
        this.option = option;
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
}
