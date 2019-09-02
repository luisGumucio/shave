/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manaco.org.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lucho
 */
@Document
public class Process {
    
    @Id
    private String id;

    private LocalDate processTime;

    private int numberProcess;

    private List<TransactionOption> transactionOptions;

    private boolean isActive;

    public Process() {
        processTime = LocalDate.now();
        isActive = true;
        transactionOptions = new ArrayList<>();
    }

    public Process(int numberProcess) {
        this.numberProcess = numberProcess;
        processTime = LocalDate.now();
        isActive = true;
        transactionOptions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getProcessTime() {
        return processTime;
    }

    public void setProcessTime(LocalDate processTime) {
        this.processTime = processTime;
    }

    public List<TransactionOption> getTransactionOptions() {
        return transactionOptions;
    }

    public void setTransactionOptions(List<TransactionOption> transactionOptions) {
        this.transactionOptions = transactionOptions;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getNumberProcess() {
        return numberProcess;
    }

    public void setNumberProcess(int numberProcess) {
        this.numberProcess = numberProcess;
    }

    public void addTransaction(TransactionOption option) {
        transactionOptions.add(option);
    }
}
