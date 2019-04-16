/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manaco.org.common.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author lucho
 */
@Entity
public class Proccess {
    
    @Id
    @GeneratedValue
    private int id;
    private boolean active;
    @Enumerated(EnumType.STRING)
    private TransactionOption type;
    private int numberProcess;

    public Proccess(int numberProcess, TransactionOption type) {
        this.numberProcess = numberProcess;
        this.type = type;
        active = true;
    }

    public Proccess() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionOption getType() {
        return type;
    }

    public void setType(TransactionOption type) {
        this.type = type;
    }

    public int getNumberProcess() {
        return numberProcess;
    }

    public void setNumberProcess(int numberProcess) {
        this.numberProcess = numberProcess;
    }
    
    
}
