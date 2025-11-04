package com.tallerwebi.dominio;

import java.util.Set;

import javax.persistence.*;

@Entity
public class PartidoEquipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partido_id")
    private Partido partido;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @Column(nullable = false)
    private Integer points;

    // Constructor por defecto para JPA
    public PartidoEquipo() {
    }

    public PartidoEquipo(Partido partido, Equipo equipo, Integer points) {
        this.partido = partido;
        this.equipo = equipo;
        this.points = points;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Integer getPoints() {
        return points;
    }

    public Set<EquipoJugador> getJugadores() {
        return equipo != null ? equipo.getJugadores() : java.util.Collections.emptySet();
    }

    public void setPoints(Integer points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points must be greater than or equal to 0");
        }
        this.points = points;
    }
}