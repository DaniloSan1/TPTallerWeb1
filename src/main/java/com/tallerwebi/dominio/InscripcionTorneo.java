package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class InscripcionTorneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Torneo
    @ManyToOne
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;

    // Relación con Equipo
    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    // Datos adicionales de la inscripción
    private LocalDateTime fechaInscripcion;


    @PrePersist
    public void prePersist() {
        if (fechaInscripcion == null) {
            fechaInscripcion = LocalDateTime.now();
        }
    }

    // Getters y setters
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public Torneo getTorneo() { 
        return torneo; 
    }
    public void setTorneo(Torneo torneo) { 
        this.torneo = torneo; 
    }

    public Equipo getEquipo() { 
        return equipo; 
    }
    public void setEquipo(Equipo equipo) { 
        this.equipo = equipo; 
    }

    public LocalDateTime getFechaInscripcion() { 
        return fechaInscripcion; 
    }
    public void setFechaInscripcion(LocalDateTime fechaInscripcion) { 
        this.fechaInscripcion = fechaInscripcion; 
    }
}