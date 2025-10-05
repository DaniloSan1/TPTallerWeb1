package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "calificador_id", nullable = false)
    private Usuario calificador;

    @ManyToOne
    @JoinColumn(name = "calificado_id", nullable = false)
    private Usuario calificado;

    @ManyToOne
    @JoinColumn(name = "partido_id", nullable = false)
    private Partido partido;

    private Integer puntuacion; // 1-5 estrellas
    private String comentario;
    private LocalDateTime fechaCalificacion;

    // Constructor por defecto para JPA
    public Calificacion() {
        this.fechaCalificacion = LocalDateTime.now();
    }

    public Calificacion(Usuario calificador, Usuario calificado, Partido partido, Integer puntuacion) {
        this.calificador = calificador;
        this.calificado = calificado;
        this.partido = partido;
        this.puntuacion = puntuacion;
        this.fechaCalificacion = LocalDateTime.now();

        // Validar puntuación
        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }
    }

    public Calificacion(Usuario calificador, Usuario calificado, Partido partido, Integer puntuacion,
            String comentario) {
        this(calificador, calificado, partido, puntuacion);
        this.comentario = comentario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getCalificador() {
        return calificador;
    }

    public void setCalificador(Usuario calificador) {
        this.calificador = calificador;
    }

    public Usuario getCalificado() {
        return calificado;
    }

    public void setCalificado(Usuario calificado) {
        this.calificado = calificado;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Integer getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(Integer puntuacion) {
        if (puntuacion < 1 || puntuacion > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5");
        }
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(LocalDateTime fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }
}