package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Goles")
public class Gol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partido_id", nullable = false)
    private Partido partido;

    @ManyToOne
    @JoinColumn(name = "equipo_jugador_id", nullable = false)
    private EquipoJugador equipoJugador;

    @Column(nullable = false)
    private Integer cantidad = 1;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    // Constructores
    public Gol() {
    }

    public Gol(Partido partido, EquipoJugador equipoJugador, Integer cantidad) {
        this.partido = partido;
        this.equipoJugador = equipoJugador;
        this.cantidad = cantidad;
    }

    // Getters y setters
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

    public EquipoJugador getEquipoJugador() {
        return equipoJugador;
    }

    public void setEquipoJugador(EquipoJugador equipoJugador) {
        this.equipoJugador = equipoJugador;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}