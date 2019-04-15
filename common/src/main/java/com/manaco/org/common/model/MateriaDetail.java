package com.manaco.org.common.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( value="materialDetail" )
@JsonDeserialize(as = MateriaDetail.class)
public class MateriaDetail extends TransactionDetail {

    private int wareHouseId;
    private int idtype;
    private String description;
    private int seccion;
    private String nroAccount;

    public String getNroAccount() {
        return nroAccount;
    }

    public void setNroAccount(String nroAccount) {
        this.nroAccount = nroAccount;
    }

    public int getSeccion() {
        return seccion;
    }

    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(int wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    public int getIdtype() {
        return idtype;
    }

    public void setIdtype(int idtype) {
        this.idtype = idtype;
    }
}
