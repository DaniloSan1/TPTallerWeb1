package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private LocalDateTime fechaReserva;
    private Boolean activa = true;
    private LocalDateTime fechaCreacion;

    
    public Reserva() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Reserva(Horario horario, Usuario usuario, LocalDateTime fechaReserva) {
        this.horario = horario;
        this.usuario = usuario;
        this.fechaReserva = fechaReserva;
        this.activa = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

   
    public Cancha getCancha() {
        return horario != null ? horario.getCancha() : null;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    
    public LocalDateTime getFechaHoraInicio() {
        if (horario == null || fechaReserva == null)
            return null;
        return fechaReserva.toLocalDate().atTime(horario.getHoraInicio());
    }

    public LocalDateTime getFechaHoraFin() {
        if (horario == null || fechaReserva == null)
            return null;
        return fechaReserva.toLocalDate().atTime(horario.getHoraFin());
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    
    public void cancelar() {
        this.activa = false;
    }

    public boolean estaActiva() {
        return this.activa && LocalDateTime.now().isBefore(this.getFechaHoraInicio());
    }

    public boolean conflictaCon(LocalDateTime inicio, LocalDateTime fin) {
        LocalDateTime inicioReserva = this.getFechaHoraInicio();
        LocalDateTime finReserva = this.getFechaHoraFin();
        return this.activa && inicioReserva != null && finReserva != null &&
                !(fin.isBefore(inicioReserva) || inicio.isAfter(finReserva));
    }
}