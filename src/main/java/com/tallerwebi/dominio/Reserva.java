package com.tallerwebi.dominio;

public class Reserva {
    private String horario;
    private String usuario;

    public Reserva(String horario, String usuario) {
        this.horario = horario;
        this.usuario = usuario;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
