package com.tallerwebi.dominio;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Partido {
    private Long id;
    private String titulo;
    private Zona zona;
    private Nivel nivel;
    private LocalDateTime fecha;
    private int cupoMaximo;
    private final Set<Long> participantes = new HashSet<>();

    public Partido(Long id, String titulo, Zona zona, Nivel nivel, LocalDateTime fecha, int cupoMaximo) {
        this.id = id; this.titulo = titulo; this.zona = zona; this.nivel = nivel;
        this.fecha = fecha; this.cupoMaximo = cupoMaximo;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public Zona getZona() { return zona; }
    public Nivel getNivel() { return nivel; }
    public LocalDateTime getFecha() { return fecha; }
    public int getCupoMaximo() { return cupoMaximo; }
    public int getCupoDisponible() { return Math.max(0, cupoMaximo - participantes.size()); }
    public boolean tieneCupo() { return getCupoDisponible() > 0; }

    // helpers mínimos por si después usás "unirse"
    public Set<Long> getParticipantes() { return participantes; }
    public boolean participa(Long usuarioId){ return usuarioId!=null && participantes.contains(usuarioId); }
    public boolean unir(Long usuarioId){
        if(usuarioId==null || participa(usuarioId) || !tieneCupo()) return false;
        return participantes.add(usuarioId);
    }
}
