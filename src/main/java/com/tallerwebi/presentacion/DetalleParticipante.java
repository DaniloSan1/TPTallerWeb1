package com.tallerwebi.presentacion;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tallerwebi.dominio.Equipo;
import com.tallerwebi.dominio.PartidoParticipante;

public class DetalleParticipante {
    private Long id;
    private Long idUsuario;
    private String nombreCompleto;
    private String posicionFavorita;
    private String equipo;
    private LocalDateTime fechaUnion;

    public DetalleParticipante(PartidoParticipante partidoParticipante) {
        this.id = partidoParticipante.getId();
        this.idUsuario = partidoParticipante.getUsuario().getId();
        this.nombreCompleto = partidoParticipante.getUsuario().getNombreCompleto();
        this.posicionFavorita = partidoParticipante.getUsuario().getPosicionFavorita();
        this.equipo = partidoParticipante.getEquipo().toString();
        this.fechaUnion = partidoParticipante.getFechaUnion();
    }

    public Long getId() {
        return id;
    }

    public Long getIdUsuario() {
        return idUsuario;
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
}
