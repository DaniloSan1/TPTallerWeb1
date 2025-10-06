package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Partido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Zona zona;
    @Enumerated(EnumType.STRING)
    private Nivel nivel;
    private LocalDateTime fecha;
    private int cupoMaximo;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "cancha_id")
    private Cancha cancha;
    @ManyToOne
    @JoinColumn(name = "creador_id")
    private Usuario creador;

    @OneToMany(mappedBy = "partido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PartidoParticipante> participantes = new HashSet<>();

    // Constructor por defecto para JPA
    public Partido() {
    }

    public Partido(String titulo, String descripcion, Zona zona, Nivel nivel, LocalDateTime fecha, int cupoMaximo,
            Cancha cancha, Usuario creador) {
        this.titulo = titulo;
        this.zona = zona;
        this.nivel = nivel;
        this.fecha = fecha;
        this.cupoMaximo = cupoMaximo;
        this.cancha = cancha;
        this.creador = creador;
        this.descripcion = descripcion;
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

    public int getCupoDisponible() {
        return Math.max(0, cupoMaximo - participantes.size());
    }

    public boolean tieneCupo() {
        return getCupoDisponible() > 0;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public Usuario getCreador() {
        return creador;
    }

    public void setCreador(Usuario creador) {
        this.creador = creador;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    // helpers para manejar participantes
    public Set<PartidoParticipante> getParticipantes() {
        return participantes;
    }

    public Set<Long> getParticipantesIds() {
        return participantes.stream()
                .map(pp -> pp.getUsuario().getId())
                .collect(java.util.stream.Collectors.toSet());
    }
}
