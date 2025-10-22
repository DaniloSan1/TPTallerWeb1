package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.PartidoParticipante;

public class DetalleParticipante {
    private Long idUsuario;
    private String nombreCompleto;
    private String posicionFavorita;

    public DetalleParticipante(PartidoParticipante partidoParticipante) {
        this.idUsuario = partidoParticipante.getUsuario().getId();
        this.nombreCompleto = partidoParticipante.getUsuario().getNombreCompleto();
        this.posicionFavorita = partidoParticipante.getUsuario().getPosicionFavorita();
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
}
