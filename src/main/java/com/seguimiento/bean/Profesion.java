package com.seguimiento.bean;

public class Profesion {

    private Integer idProfesion;
    private String nomProfesion;
    private String estadoProfesion;

    // Constructores
    public Profesion() {}

    public Profesion(String nomProfesion) {
        this.nomProfesion = nomProfesion;
    }

    public Profesion(Integer idProfesion, String nomProfesion, String estadoProfesion) {
        this.idProfesion = idProfesion;
        this.nomProfesion = nomProfesion;
        this.estadoProfesion = estadoProfesion;
    }

    // Getters y Setters
    public Integer getIdProfesion() { return idProfesion; }
    public void setIdProfesion(Integer idProfesion) { this.idProfesion = idProfesion; }

    public String getNomProfesion() { return nomProfesion; }
    public void setNomProfesion(String nomProfesion) { this.nomProfesion = nomProfesion; }

    public String getEstadoProfesion() { return estadoProfesion; }
    public void setEstadoProfesion(String estadoProfesion) { this.estadoProfesion = estadoProfesion; }
}

