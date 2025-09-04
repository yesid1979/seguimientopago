package com.seguimiento.bean;

public class Predio {

    // Atributos básicos
    private int nroPredio;
    private String idPredio;
    private String numreciboPredio;
    private String matriculaPredio;
    private String veredaBarrio;
    private String dirPredio;
    private double valorPendiente;
    private String observacion;
    private String vigenciaPredio;
    private double valorEnviado;
    private String estadoPredio;

    // Relación con Contribuyente (Llave foránea)
    private Contribuyente contribuyente;

    // Constructor vacío
    public Predio() {
    }

    // Constructor con parámetros
    public Predio(int nroPredio, String idPredio, String numreciboPredio,
            String matriculaPredio, String veredaBarrio, String dirPredio,
            double valorPendiente, String observacion, String vigenciaPredio,
            double valorEnviado, String estadoPredio, Contribuyente contribuyente) {
        this.nroPredio = nroPredio;
        this.idPredio = idPredio;
        this.numreciboPredio = numreciboPredio;
        this.matriculaPredio = matriculaPredio;
        this.veredaBarrio = veredaBarrio;
        this.dirPredio = dirPredio;
        this.valorPendiente = valorPendiente;
        this.observacion = observacion;
        this.vigenciaPredio = vigenciaPredio;
        this.valorEnviado = valorEnviado;
        this.estadoPredio = estadoPredio;
        this.contribuyente = contribuyente;
    }

    // Getters y Setters
    public int getNroPredio() {
        return nroPredio;
    }

    public void setNroPredio(int nroPredio) {
        this.nroPredio = nroPredio;
    }

    public String getIdPredio() {
        return idPredio;
    }

    public void setIdPredio(String idPredio) {
        this.idPredio = idPredio;
    }

    public String getNumreciboPredio() {
        return numreciboPredio;
    }

    public void setNumreciboPredio(String numreciboPredio) {
        this.numreciboPredio = numreciboPredio;
    }

    public String getMatriculaPredio() {
        return matriculaPredio;
    }

    public void setMatriculaPredio(String matriculaPredio) {
        this.matriculaPredio = matriculaPredio;
    }

    public String getVeredaBarrio() {
        return veredaBarrio;
    }

    public void setVeredaBarrio(String veredaBarrio) {
        this.veredaBarrio = veredaBarrio;
    }

    public String getDirPredio() {
        return dirPredio;
    }

    public void setDirPredio(String dirPredio) {
        this.dirPredio = dirPredio;
    }

    public double getValorPendiente() {
        return valorPendiente;
    }

    public void setValorPendiente(double valorPendiente) {
        this.valorPendiente = valorPendiente;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getVigenciaPredio() {
        return vigenciaPredio;
    }

    public void setVigenciaPredio(String vigenciaPredio) {
        this.vigenciaPredio = vigenciaPredio;
    }

    public double getValorEnviado() {
        return valorEnviado;
    }

    public void setValorEnviado(double valorEnviado) {
        this.valorEnviado = valorEnviado;
    }

    public String getEstadoPredio() {
        return estadoPredio;
    }

    public void setEstadoPredio(String estadoPredio) {
        this.estadoPredio = estadoPredio;
    }

    // Getter y Setter para la relación con Contribuyente
    public Contribuyente getContribuyente() {
        return contribuyente;
    }

    public void setContribuyente(Contribuyente contribuyente) {
        this.contribuyente = contribuyente;
    }

    // Método para obtener el ID del contribuyente (útil para consultas SQL)
    public Integer getCodContribuyente() {
        return contribuyente != null ? contribuyente.getIdContribuyente() : null;
    }

    @Override
    public String toString() {
        return "Predio{"
                + "nroPredio=" + nroPredio
                + ", idPredio='" + idPredio + '\''
                + ", numreciboPredio='" + numreciboPredio + '\''
                + ", matriculaPredio='" + matriculaPredio + '\''
                + ", veredaBarrio='" + veredaBarrio + '\''
                + ", dirPredio='" + dirPredio + '\''
                + ", valorPendiente=" + valorPendiente
                + ", observacion='" + observacion + '\''
                + ", vigenciaPredio='" + vigenciaPredio + '\''
                + ", valorEnviado=" + valorEnviado
                + ", estadoPredio='" + estadoPredio + '\''
                + ", contribuyente=" + (contribuyente != null ? contribuyente.getNomContribuyente() : "N/A")
                + '}';
    }
}
