package com.tallerwebi.dominio;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class NotificacionDeUsuario {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

private String mensaje;

private Boolean leida = false;

private LocalDate fecha;

private NotificacionEnum tipoDeNotificacion;

@Nullable
private Long referenciaIdPartido;

@ManyToOne
@JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; //el que recibe la notificacion

    public NotificacionDeUsuario() {}

    public NotificacionDeUsuario(Usuario usuario, String mensaje, NotificacionEnum tipoDeNotificacion,  Long referenciaIdPartido) {
        this.mensaje = mensaje;
        this.usuario = usuario;
        this.tipoDeNotificacion = tipoDeNotificacion;
        this.referenciaIdPartido = referenciaIdPartido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getLeida() {
        return leida;
    }

    public void setLeida(Boolean leida) {
        this.leida = leida;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public NotificacionEnum getTipoDeNotificacion() {
        return tipoDeNotificacion;
    }

    public  void setTipoDeNotificacion(NotificacionEnum tipoDeNotificacion) {
        this.tipoDeNotificacion = tipoDeNotificacion;
    }

    public Long getReferenciaIdPartido() {
        return referenciaIdPartido;
    }

    public void setReferenciaIdPartido(Long referenciaIdPartido) {
        this.referenciaIdPartido = referenciaIdPartido;
    }
}
