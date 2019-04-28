package com.manaco.org.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue( value="replacementDetail" )
@JsonDeserialize(as = ReplacementDetail.class)
public class ReplacementDetail extends TransactionDetail {

    private int typeReplacement;
    
    @Column(length=1)
    private String nro;
    private String descriptionReplacement;
    private String seccionReplacement;
    private String nroAccountReplacement;

    public int getTypeReplacement() {
        return typeReplacement;
    }

    public void setTypeReplacement(int typeReplacement) {
        this.typeReplacement = typeReplacement;
    }

    public String getNro() {
        return nro;
    }

    public void setNro(String nro) {
        this.nro = nro;
    }

    public String getDescriptionReplacement() {
        return descriptionReplacement;
    }

    public void setDescriptionReplacement(String descriptionReplacement) {
        this.descriptionReplacement = descriptionReplacement;
    }

    public String getSeccionReplacement() {
        return seccionReplacement;
    }

    public void setSeccionReplacement(String seccionReplacement) {
        this.seccionReplacement = seccionReplacement;
    }

    public String getNroAccountReplacement() {
        return nroAccountReplacement;
    }

    public void setNroAccountReplacement(String nroAccountReplacement) {
        this.nroAccountReplacement = nroAccountReplacement;
    }
}
