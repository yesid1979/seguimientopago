package com.seguimiento.bean;

public class Rol {

    private Integer idRol;
    private String nomRol;
    private String descRol;
    private String estadoRol;

    // Constructores
    public Rol() {
    }

    public Rol(String nomRol) {
        this.nomRol = nomRol;
    }

    
        
    public Rol(Integer idRol, String nomRol, String descRol, String estadoRol) {
        this.idRol = idRol;
        this.nomRol = nomRol;
        this.descRol = descRol;
        this.estadoRol = estadoRol;
    }

    // Getters y Setters
    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNomRol() {
        return nomRol;
    }

    public void setNomRol(String nomRol) {
        this.nomRol = nomRol;
    }

    public String getDescRol() {
        return descRol;
    }

    public void setDescRol(String descRol) {
        this.descRol = descRol;
    }

    public String getEstadoRol() {
        return estadoRol;
    }

    public void setEstadoRol(String estadoRol) {
        this.estadoRol = estadoRol;
    }
}
