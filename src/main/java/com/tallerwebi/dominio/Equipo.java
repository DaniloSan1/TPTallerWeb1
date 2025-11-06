package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "creado_por_id")
    private Usuario creadoPor;

    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<EquipoJugador> jugadores = new HashSet<>();

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PartidoEquipo> partidos = new HashSet<>();

    // Constructor por defecto para JPA
    public Equipo() {
    }

    public Equipo(String nombre, Usuario creadoPor, LocalDateTime fechaCreacion) {
        this.nombre = nombre;
        this.creadoPor = creadoPor;
        this.fechaCreacion = fechaCreacion;
    }

    @PrePersist
    public void setFechaCreacionDefault() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Usuario creadoPor) {
        this.creadoPor = creadoPor;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Set<EquipoJugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(Set<EquipoJugador> jugadores) {
        this.jugadores = jugadores;
    }

    public Set<PartidoEquipo> getPartidos() {
        return partidos;
    }

    public void setPartidos(Set<PartidoEquipo> partidos) {
        this.partidos = partidos;
    }

    public boolean yaExisteJugador(Long usuarioId) {
        return jugadores.stream()
                .anyMatch(ej -> ej.getUsuario().getId().equals(usuarioId));
    }

    public void agregarJugador(EquipoJugador equipoJugador) {
        jugadores.add(equipoJugador);
        equipoJugador.setEquipo(this);
    }
}