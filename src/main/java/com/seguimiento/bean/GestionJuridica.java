package com.seguimiento.bean;

public class GestionJuridica {

    private int idGestion;             // SERIAL PRIMARY KEY
    private String fechaMandamiento;   // VARCHAR(60)
    private String numProceso;         // VARCHAR(100)
    private String etapaProceso;       // VARCHAR(100)
    private double valorRecuperado;    // NUMERIC(15,2)
    private Integer codPredio;         // FK a predios(nro_predio)

    // Constructor vacío
    public GestionJuridica() {
    }

    // Constructor con parámetros
    public GestionJuridica(int idGestion, String fechaMandamiento, String numProceso,
            String etapaProceso, double valorRecuperado, Integer codPredio) {
        this.idGestion = idGestion;
        this.fechaMandamiento = fechaMandamiento;
        this.numProceso = numProceso;
        this.etapaProceso = etapaProceso;
        this.valorRecuperado = valorRecuperado;
        this.codPredio = codPredio;
    }

    // Getters y Setters
    public int getIdGestion() {
        return idGestion;
    }

    public void setIdGestion(int idGestion) {
        this.idGestion = idGestion;
    }

    public String getFechaMandamiento() {
        return fechaMandamiento;
    }

    public void setFechaMandamiento(String fechaMandamiento) {
        this.fechaMandamiento = fechaMandamiento;
    }

    public String getNumProceso() {
        return numProceso;
    }

    public void setNumProceso(String numProceso) {
        this.numProceso = numProceso;
    }

    public String getEtapaProceso() {
        return etapaProceso;
    }

    public void setEtapaProceso(String etapaProceso) {
        this.etapaProceso = etapaProceso;
    }

    public double getValorRecuperado() {
        return valorRecuperado;
    }

    public void setValorRecuperado(double valorRecuperado) {
        this.valorRecuperado = valorRecuperado;
    }

    public Integer getCodPredio() {
        return codPredio;
    }

    public void setCodPredio(Integer codPredio) {
        this.codPredio = codPredio;
    }
}
