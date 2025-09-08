package com.seguimiento.bean;

public class Dashboard {

    private int totalContribuyentes;
    private int totalPredios;
    private int totalNotificados;
    private int totalCoactivos;

    public Dashboard() {
    }

    public Dashboard(int totalContribuyentes, int totalPredios, int totalNotificados, int totalCoactivos) {
        this.totalContribuyentes = totalContribuyentes;
        this.totalPredios = totalPredios;
        this.totalNotificados = totalNotificados;
        this.totalCoactivos = totalCoactivos;
    }  
    // Getters y Setters
    public int getTotalContribuyentes() {
        return totalContribuyentes;
    }

    public void setTotalContribuyentes(int totalContribuyentes) {
        this.totalContribuyentes = totalContribuyentes;
    }

    public int getTotalPredios() {
        return totalPredios;
    }

    public void setTotalPredios(int totalPredios) {
        this.totalPredios = totalPredios;
    }

    public int getTotalNotificados() {
        return totalNotificados;
    }

    public void setTotalNotificados(int totalNotificados) {
        this.totalNotificados = totalNotificados;
    }

    public int getTotalCoactivos() {
        return totalCoactivos;
    }

    public void setTotalCoactivos(int totalCoactivos) {
        this.totalCoactivos = totalCoactivos;
    }
}
