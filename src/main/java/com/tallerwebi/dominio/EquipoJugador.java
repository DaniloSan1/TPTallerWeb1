package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class EquipoJugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "fecha_union")
    private LocalDateTime fechaUnion;

    @PrePersist
    public void setFechaUnionDefault() {
        if (fechaUnion == null) {
            fechaUnion = LocalDateTime.now();
        }
    }

    // Constructor por defecto para JPA
    public EquipoJugador() {
    }

    public EquipoJugador(Equipo equipo, Usuario usuario) {
        this.equipo = equipo;
        this.usuario = usuario;
        this.fechaUnion = LocalDateTime.now();
    }

    public EquipoJugador(Equipo equipo, Usuario usuario, LocalDateTime fechaUnion) {
        this.equipo = equipo;
        this.usuario = usuario;
        this.fechaUnion = fechaUnion;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaUnion() {
        return fechaUnion;
    }

    public void setFechaUnion(LocalDateTime fechaUnion) {
        this.fechaUnion = fechaUnion;
    }
}