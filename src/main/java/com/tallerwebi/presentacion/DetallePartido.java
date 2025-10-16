package com.tallerwebi.presentacion;

import java.time.LocalDateTime;

import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Zona;

public class DetallePartido {
    private Long id;
    private String titulo;
    private Zona zona;
    private Nivel nivel;
    private LocalDateTime fecha;
    private int cupoMaximo;
    private String descripcion;
    private Long canchaId;
    private Long creadorId;
    private String cancha;
    private String creador;
    private Boolean hayCupo;
    private Boolean yaParticipa;
    private boolean esCreador;
    private Long reservaId;
    private Long horarioId;

    public DetallePartido(Partido partido, Usuario usuario) {
        this.id = partido.getId();
        this.titulo = partido.getTitulo();
        this.zona = partido.getZona();
        this.nivel = partido.getNivel();
        this.fecha = partido.getFecha();
        this.cupoMaximo = partido.getCupoMaximo();
        this.descripcion = partido.getDescripcion();
        this.canchaId = partido.getReserva().getCancha().getId();
        this.creadorId = partido.getCreador().getId();
        this.cancha = partido.getReserva().getCancha().getNombre();
        this.creador = partido.getCreador().getNombre();
        this.hayCupo = partido.tieneCupo();
        this.yaParticipa = partido.validarParticipanteExistente(usuario.getId());
        this.esCreador = partido.esCreador(usuario.getEmail());
        this.reservaId = partido.getReserva().getId();
        this.horarioId = partido.getReserva().getHorario().getId();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Zona getZona() {
        return zona;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Long getCanchaId() {
        return canchaId;
    }

    public Long getCreadorId() {
        return creadorId;
    }

    public String getCancha() {
        return cancha;
    }

    public String getCreador() {
        return creador;
    }

    public Boolean getHayCupo() {
        return hayCupo;
    }

    public Boolean getYaParticipa() {
        return yaParticipa;
    }

    public boolean getEsCreador() {
        return esCreador;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public Long getHorarioId() {
        return horarioId;
    }
}
