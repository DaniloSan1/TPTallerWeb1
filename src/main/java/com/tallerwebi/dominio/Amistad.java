package com.tallerwebi.dominio;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Amistad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAmistad;

    @ManyToOne
    @JoinColumn(name = "usuario_1_id")
    private Usuario usuario1;
    @ManyToOne
    @JoinColumn(name = "usuario_2_id")
    private Usuario usuario2;

    public Usuario getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(Usuario usuario2) {
        this.usuario2 = usuario2;
    }

    public Usuario getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(Usuario usuario1) {
        this.usuario1 = usuario1;
    }

    @Enumerated(EnumType.STRING)
    private EstadoDeAmistad estadoDeAmistad;

    private LocalDate fechaSolicitud;

    public Amistad() {}

    public Amistad(Usuario usuario1, Usuario usuario2){ //Usuario 1 le manda solicitud a Usuario 2
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.estadoDeAmistad = EstadoDeAmistad.PENDIENTE;
        this.fechaSolicitud = LocalDate.now();
    }

    public Long getIdAmistad() {
        return idAmistad;
    }

    public void setIdAmistad(Long id) {
        this.idAmistad = id;
    }

    public EstadoDeAmistad getEstadoDeAmistad() {
        return estadoDeAmistad;
    }

    public void setEstadoDeAmistad(EstadoDeAmistad estadoDeAmistad) {
        this.estadoDeAmistad = estadoDeAmistad;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
}
