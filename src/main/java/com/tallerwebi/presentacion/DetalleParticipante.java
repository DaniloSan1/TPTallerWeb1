package com.tallerwebi.presentacion;

import java.time.LocalDateTime;

import com.tallerwebi.dominio.EquipoJugador;

public class DetalleParticipante {
    private Long id;
    private Long idUsuario;
    private String username;
    private String nombreCompleto;
    private String posicionFavorita;
    private Long idEquipo;
    private String equipo;
    private LocalDateTime fechaUnion;

    public DetalleParticipante(EquipoJugador equipoJugador) {
        this.id = equipoJugador.getId();
        this.idUsuario = equipoJugador.getUsuario().getId();
        this.nombreCompleto = equipoJugador.getUsuario().getNombreCompleto();
        this.posicionFavorita = equipoJugador.getUsuario().getPosicionFavorita();
        this.equipo = equipoJugador.getEquipo().getNombre();
        this.idEquipo = equipoJugador.getEquipo().getId();
        this.fechaUnion = equipoJugador.getFechaUnion();
        this.username = equipoJugador.getUsuario().getUsername();
    }

    public Long getId() {
        return id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getPosicionFavorita() {
        return posicionFavorita;
    }

    public String getEquipo() {
        return equipo;
    }

    public LocalDateTime getFechaUnion() {
        return fechaUnion;
    }

    public String getUsername() {
        return username;
    }
}