package com.seguimiento.bean;

public class Cobro {

    private int idCobro;               // SERIAL PRIMARY KEY
    private int numIntentos;           // integer DEFAULT 0
    private String compromiso;         // TEXT
    private String fechaSeguimiento;   // VARCHAR(60)
    private double valorAcordado;      // NUMERIC(15,2)
    private Integer codPredio;         // integer (nullable)

    // Constructor vacío
    public Cobro() {
    }

    // Constructor con parámetros
    public Cobro(int idCobro, int numIntentos, String compromiso, String fechaSeguimiento, double valorAcordado, Integer codPredio) {
        this.idCobro = idCobro;
        this.numIntentos = numIntentos;
        this.compromiso = compromiso;
        this.fechaSeguimiento = fechaSeguimiento;
        this.valorAcordado = valorAcordado;
        this.codPredio = codPredio;
    }

    // Getters y Setters
    public int getIdCobro() {
        return idCobro;
    }

    public void setIdCobro(int idCobro) {
        this.idCobro = idCobro;
    }

    public int getNumIntentos() {
        return numIntentos;
    }

    public void setNumIntentos(int numIntentos) {
        this.numIntentos = numIntentos;
    }

    public String getCompromiso() {
        return compromiso;
    }

    public void setCompromiso(String compromiso) {
        this.compromiso = compromiso;
    }

    public String getFechaSeguimiento() {
        return fechaSeguimiento;
    }

    public void setFechaSeguimiento(String fechaSeguimiento) {
        this.fechaSeguimiento = fechaSeguimiento;
    }

    public double getValorAcordado() {
        return valorAcordado;
    }

    public void setValorAcordado(double valorAcordado) {
        this.valorAcordado = valorAcordado;
    }

    public Integer getCodPredio() {
        return codPredio;
    }

    public void setCodPredio(Integer codPredio) {
        this.codPredio = codPredio;
    }
}
