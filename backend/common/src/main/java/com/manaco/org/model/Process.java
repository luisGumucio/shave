/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manaco.org.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lucho
 */
@Entity
public class Process {
    
    @Id
    @GeneratedValue
    private int id;

    private LocalDate processTime;

    @Enumerated(EnumType.STRING)
    private TransactionOption transactionOption;

    private int numberProcess;

    @OneToMany(
            mappedBy = "transactions",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Transaction> transactionList;

    private boolean isActive;

    public Process() {
    }

    public Process(int numberProcess, TransactionOption transactionOption) {
        this.transactionOption = transactionOption;
        this.numberProcess = numberProcess;
        processTime = LocalDate.now();
        isActive = true;
//        transactionList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getProcessTime() {
        return processTime;
    }

    public void setProcessTime(LocalDate processTime) {
        this.processTime = processTime;
    }

    public TransactionOption getTransactionOption() {
        return transactionOption;
    }

    public void setTransactionOption(TransactionOption transactionOption) {
        this.transactionOption = transactionOption;
    }

    @JsonIgnore
    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
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

    public void addComment(Transaction transaction) {
        transactionList.add(transaction);
        transaction.setTransactions(this);
    }

    public void removeComment(Transaction transaction) {
        transactionList.remove(transaction);
        transaction.setTransactions(null);
    }
}
