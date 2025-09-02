package com.seguimiento.bean;

import java.io.Serializable;

public class Contribuyente implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idContribuyente;
    private String nomContribuyente;
    private String celContribuyente;
    private String correoContribuyente;
    private String dirContribuyente;
    private String estadoContribuyente;

    // Constructor vacío
    public Contribuyente() {
    }

    // Constructor con parámetros
    public Contribuyente(int idContribuyente, String nomContribuyente, String celContribuyente,
            String correoContribuyente, String dirContribuyente, String estadoContribuyente) {
        this.idContribuyente = idContribuyente;
        this.nomContribuyente = nomContribuyente;
        this.celContribuyente = celContribuyente;
        this.correoContribuyente = correoContribuyente;
        this.dirContribuyente = dirContribuyente;
        this.estadoContribuyente = estadoContribuyente;
    }

    // Getters y Setters
    public int getIdContribuyente() {
        return idContribuyente;
    }

    public void setIdContribuyente(int idContribuyente) {
        this.idContribuyente = idContribuyente;
    }

    public String getNomContribuyente() {
        return nomContribuyente;
    }

    public void setNomContribuyente(String nomContribuyente) {
        this.nomContribuyente = nomContribuyente;
    }

    public String getCelContribuyente() {
        return celContribuyente;
    }

    public void setCelContribuyente(String celContribuyente) {
        this.celContribuyente = celContribuyente;
    }

    public String getCorreoContribuyente() {
        return correoContribuyente;
    }

    public void setCorreoContribuyente(String correoContribuyente) {
        this.correoContribuyente = correoContribuyente;
    }

    public String getDirContribuyente() {
        return dirContribuyente;
    }

    public void setDirContribuyente(String dirContribuyente) {
        this.dirContribuyente = dirContribuyente;
    }

    public String getEstadoContribuyente() {
        return estadoContribuyente;
    }

    public void setEstadoContribuyente(String estadoContribuyente) {
        this.estadoContribuyente = estadoContribuyente;
    }

    @Override
    public String toString() {
        return "Contribuyente{"
                + "idContribuyente=" + idContribuyente
                + ", nomContribuyente='" + nomContribuyente + '\''
                + ", celContribuyente='" + celContribuyente + '\''
                + ", correoContribuyente='" + correoContribuyente + '\''
                + ", dirContribuyente='" + dirContribuyente + '\''
                + ", estadoContribuyente='" + estadoContribuyente + '\''
                + '}';
    }
}
