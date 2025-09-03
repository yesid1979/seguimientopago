package com.seguimiento.bean;

public class Usuario {

    private Integer idUsuario;
    private String cedUsuario;
    private String nomUsuario;
    private String telUsuario;
    private String celUsuario;
    private String sexoUsuario;
    private String dirUsuario;
    private String nacUsuario;
    private String emailUsuario;
    private String estadoUsuario;
    private String loginUsuario;
    private String passwordUsuario;
    private String tpUsuario;
    private String fotoUsuario;

    // Relaciones
    private Profesion profesion; // FK: cod_profesion
    private Rol rol;             // FK: cod_rol

    // Constructores
    public Usuario() {
    }

    public Usuario(Integer idUsuario, String cedUsuario, String nomUsuario, String telUsuario, String celUsuario,
            String sexoUsuario, String dirUsuario, String nacUsuario, String emailUsuario, String estadoUsuario,
            String loginUsuario, String passwordUsuario, String tpUsuario, String fotoUsuario,
            Profesion profesion, Rol rol) {
        this.idUsuario = idUsuario;
        this.cedUsuario = cedUsuario;
        this.nomUsuario = nomUsuario;
        this.telUsuario = telUsuario;
        this.celUsuario = celUsuario;
        this.sexoUsuario = sexoUsuario;
        this.dirUsuario = dirUsuario;
        this.nacUsuario = nacUsuario;
        this.emailUsuario = emailUsuario;
        this.estadoUsuario = estadoUsuario;
        this.loginUsuario = loginUsuario;
        this.passwordUsuario = passwordUsuario;
        this.tpUsuario = tpUsuario;
        this.fotoUsuario = fotoUsuario;
        this.profesion = profesion;
        this.rol = rol;
    }

    // Getters y Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCedUsuario() {
        return cedUsuario;
    }

    public void setCedUsuario(String cedUsuario) {
        this.cedUsuario = cedUsuario;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getTelUsuario() {
        return telUsuario;
    }

    public void setTelUsuario(String telUsuario) {
        this.telUsuario = telUsuario;
    }

    public String getCelUsuario() {
        return celUsuario;
    }

    public void setCelUsuario(String celUsuario) {
        this.celUsuario = celUsuario;
    }

    public String getSexoUsuario() {
        return sexoUsuario;
    }

    public void setSexoUsuario(String sexoUsuario) {
        this.sexoUsuario = sexoUsuario;
    }

    public String getDirUsuario() {
        return dirUsuario;
    }

    public void setDirUsuario(String dirUsuario) {
        this.dirUsuario = dirUsuario;
    }

    public String getNacUsuario() {
        return nacUsuario;
    }

    public void setNacUsuario(String nacUsuario) {
        this.nacUsuario = nacUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(String estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public String getPasswordUsuario() {
        return passwordUsuario;
    }

    public void setPasswordUsuario(String passwordUsuario) {
        this.passwordUsuario = passwordUsuario;
    }

    public String getTpUsuario() {
        return tpUsuario;
    }

    public void setTpUsuario(String tpUsuario) {
        this.tpUsuario = tpUsuario;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public Profesion getProfesion() {
        return profesion;
    }

    public void setProfesion(Profesion profesion) {
        this.profesion = profesion;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
