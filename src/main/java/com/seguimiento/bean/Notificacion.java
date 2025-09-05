package com.seguimiento.bean;

public class Notificacion {
    private Integer idNotificacion;
    private Integer codPredio;
    private String fechaNotificacion;
    private String horaNotificacion;
    private String tipoNotificacion;
    private Double valorEnviado;
    private String estadoNotificacion;
    private String agenciaEnvio;
    private String observacion;

    // Getters y Setters
    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Integer getCodPredio() {
        return codPredio;
    }

    public void setCodPredio(Integer codPredio) {
        this.codPredio = codPredio;
    }

    public String getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(String fechaNotificacion) {
        this.fechaNotificacion = fechaNotificacion;
    }

    public String getHoraNotificacion() {
        return horaNotificacion;
    }

    public void setHoraNotificacion(String horaNotificacion) {
        this.horaNotificacion = horaNotificacion;
    }

    public String getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public Double getValorEnviado() {
        return valorEnviado;
    }

    public void setValorEnviado(Double valorEnviado) {
        this.valorEnviado = valorEnviado;
    }

    public String getEstadoNotificacion() {
        return estadoNotificacion;
    }

    public void setEstadoNotificacion(String estadoNotificacion) {
        this.estadoNotificacion = estadoNotificacion;
    }

    public String getAgenciaEnvio() {
        return agenciaEnvio;
    }

    public void setAgenciaEnvio(String agenciaEnvio) {
        this.agenciaEnvio = agenciaEnvio;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
