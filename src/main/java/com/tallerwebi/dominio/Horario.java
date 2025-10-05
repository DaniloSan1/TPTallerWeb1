package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;

@Entity
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cancha_id", nullable = false)
    private Cancha cancha;

    @Enumerated(EnumType.STRING)
    private DayOfWeek diaSemana;

    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean disponible = true;

    // Constructor por defecto para JPA
    public Horario() {
    }

    public Horario(Cancha cancha, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        this.cancha = cancha;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.disponible = true;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    // Métodos de negocio
    public boolean estaDisponibleEn(LocalDateTime fechaHora) {
        return this.disponible &&
                this.diaSemana.equals(fechaHora.getDayOfWeek()) &&
                !fechaHora.toLocalTime().isBefore(this.horaInicio) &&
                !fechaHora.toLocalTime().isAfter(this.horaFin);
    }

    public boolean conflictaCon(LocalTime inicio, LocalTime fin) {
        return !(fin.isBefore(this.horaInicio) || inicio.isAfter(this.horaFin));
    }
}