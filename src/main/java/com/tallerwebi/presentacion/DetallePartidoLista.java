package com.tallerwebi.presentacion;

import java.time.LocalDateTime;

import com.tallerwebi.dominio.Nivel;
import com.tallerwebi.dominio.Partido;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Zona;

public class DetallePartidoLista {
    private Long id;
    private String titulo;
    private Zona zona;
    private Nivel nivel;
    private LocalDateTime fecha;
    private String descripcion;
    private Long creadorId;
    private String creador;
    private Boolean hayCupo;
    private boolean esCreador;
    private int cuposDisponibles;
    private String direccion;
    private boolean finalizado;
    private int cupoMaximo;
    private String fotoCanchaUrl;

    public DetallePartidoLista(Partido partido, Usuario usuario) {
        this.id = partido.getId();
        this.titulo = partido.getTitulo();
        this.fotoCanchaUrl = partido.getReserva().getCancha().getFotos().isEmpty() ? null
                : partido.getReserva().getCancha().getFotos().iterator().next().getUrl();
        this.zona = partido.getZona();
        this.nivel = partido.getNivel();
        this.fecha = partido.getFecha();
        this.descripcion = partido.getDescripcion();
        this.creadorId = partido.getCreador().getId();
        this.creador = partido.getCreador().getNombreCompleto();
        this.finalizado = partido.getFinalizado();
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

    public String getDescripcion() {
        return descripcion;
    }

    public Long getCreadorId() {
        return creadorId;
    }

    public String getCreador() {
        return creador;
    }

    public Boolean getHayCupo() {
        return hayCupo;
    }

    public boolean getEsCreador() {
        return esCreador;
    }

    public int getCuposDisponibles() {
        return cuposDisponibles;
    }

    public String getDireccion() {
        return direccion;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public boolean getFinalizado() {
        return finalizado;
    }

    public String getFotoCanchaUrl() {
        return fotoCanchaUrl;
    }
}
