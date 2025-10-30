package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PartidoParticipante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partido_id", nullable = false)
    private Partido partido;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private Equipo equipo;

    @Column(name = "fecha_union")
    private LocalDateTime fechaUnion;

    // Constructor por defecto para JPA
    public PartidoParticipante() {
        this.equipo = Equipo.SIN_EQUIPO;
    }

    public PartidoParticipante(Partido partido, Usuario usuario, Equipo equipo) {
        this.partido = partido;
        this.usuario = usuario;
        this.equipo = equipo == null ? Equipo.SIN_EQUIPO : equipo;
        this.fechaUnion = LocalDateTime.now();
    }

    public PartidoParticipante(Partido partido, Usuario usuario, LocalDateTime fechaUnion, Equipo equipo) {
        this.partido = partido;
        this.usuario = usuario;
        this.fechaUnion = fechaUnion;
        this.equipo = equipo == null ? Equipo.SIN_EQUIPO : equipo;
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

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        PartidoParticipante that = (PartidoParticipante) o;

        if (partido != null ? !partido.getId().equals(that.partido.getId()) : that.partido != null)
            return false;
        return usuario != null ? usuario.getId().equals(that.usuario.getId()) : that.usuario == null;
    }

    @Override
    public int hashCode() {
        int result = partido != null ? partido.getId().hashCode() : 0;
        result = 31 * result + (usuario != null ? usuario.getId().hashCode() : 0);
        return result;
    }
}