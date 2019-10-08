package com.manaco.org.dto;

import java.time.LocalDate;

public class ProductDto {

    private String TIPO;
    private String ALMACEN;
    private String NRO_DOC;
    private String ARTICULO;
    private String PARES;
    private String PCOSTO;
    private LocalDate FECHA_DOC;
    private String ORIGEN;
    private String SEMANA;
    private String TABLA_ORIGEN;
    private String TIPO_MOV;


    public String getTIPO() {
        return TIPO;
    }

    public void setTIPO(String TIPO) {
        this.TIPO = TIPO;
    }

    public String getALMACEN() {
        return ALMACEN;
    }

    public void setALMACEN(String ALMACEN) {
        this.ALMACEN = ALMACEN;
    }

    public String getNRO_DOC() {
        return NRO_DOC;
    }

    public void setNRO_DOC(String NRO_DOC) {
        this.NRO_DOC = NRO_DOC;
    }

    public String getARTICULO() {
        return ARTICULO;
    }

    public void setARTICULO(String ARTICULO) {
        this.ARTICULO = ARTICULO;
    }

    public String getPARES() {
        return PARES;
    }

    public void setPARES(String PARES) {
        this.PARES = PARES;
    }

    public String getPCOSTO() {
        return PCOSTO;
    }

    public void setPCOSTO(String PCOSTO) {
        this.PCOSTO = PCOSTO;
    }



    public String getORIGEN() {
        return ORIGEN;
    }

    public void setORIGEN(String ORIGEN) {
        this.ORIGEN = ORIGEN;
    }

    public String getSEMANA() {
        return SEMANA;
    }

    public void setSEMANA(String SEMANA) {
        this.SEMANA = SEMANA;
    }

    public String getTABLA_ORIGEN() {
        return TABLA_ORIGEN;
    }

    public void setTABLA_ORIGEN(String TABLA_ORIGEN) {
        this.TABLA_ORIGEN = TABLA_ORIGEN;
    }

    public String getTIPO_MOV() {
        return TIPO_MOV;
    }

    public void setTIPO_MOV(String TIPO_MOV) {
        this.TIPO_MOV = TIPO_MOV;
    }

    public LocalDate getFECHA_DOC() {
        return FECHA_DOC;
    }

    public void setFECHA_DOC(LocalDate FECHA_DOC) {
        this.FECHA_DOC = FECHA_DOC;
    }
}
