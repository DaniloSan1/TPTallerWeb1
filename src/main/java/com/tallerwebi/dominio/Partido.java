package com.tallerwebi.dominio;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Partido {
    private Long id;
    private String titulo;
    private String descripcion;     
    private Zona zona;
    private Nivel nivel;
    private LocalDateTime fecha;
    private int cupoMaximo;         
    private Long reservaId;         
    private final Set<Long> participantes = new HashSet<>();

    public Partido(Long id, String titulo, Zona zona, Nivel nivel, LocalDateTime fecha, int cupoMaximo) {
        this(id, titulo, null, zona, nivel, fecha, cupoMaximo, null);
    }

    public Partido(Long id, String titulo, String descripcion, Zona zona, Nivel nivel,
                   LocalDateTime fecha, int cupoMaximo, Long reservaId) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.zona = zona;
        this.nivel = nivel;
        this.fecha = fecha;
        this.cupoMaximo = Math.max(0, cupoMaximo); 
        this.reservaId = reservaId;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }      
    public Zona getZona() { return zona; }
    public Nivel getNivel() { return nivel; }
    public LocalDateTime getFecha() { return fecha; }
    public int getCupoMaximo() { return cupoMaximo; }
    public Long getReservaId() { return reservaId; }            

    // Setters
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; } 
    public void setZona(Zona zona) { this.zona = zona; }
    public void setNivel(Nivel nivel) { this.nivel = nivel; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = Math.max(0, cupoMaximo); }
    public void setReservaId(Long reservaId) { this.reservaId = reservaId; }           

    public int getCupoDisponible() { return Math.max(0, cupoMaximo - participantes.size()); }
    public boolean tieneCupo() { return getCupoDisponible() > 0; }

    public Set<Long> getParticipantes() { return participantes; }
    public boolean participa(Long usuarioId){ return usuarioId!=null && participantes.contains(usuarioId); }
    public boolean unir(Long usuarioId){
        if(usuarioId==null || participa(usuarioId) || !tieneCupo()) return false;
        return participantes.add(usuarioId);
    }
}

