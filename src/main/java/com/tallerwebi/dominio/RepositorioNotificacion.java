package com.tallerwebi.dominio;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositorioNotificacion {
    public void guardar(NotificacionDeUsuario notificacionDeUsuario);
    List<NotificacionDeUsuario> obtenerListaDeNotificaciones(Usuario usuario);
    public void marcarComoLeida(Long id);
    public void eliminarNotificacion(NotificacionDeUsuario notificacionDeUsuario);
    public NotificacionDeUsuario obtenerNotificacion(Long id);
    public void actualizar(NotificacionDeUsuario notificacionDeUsuario);
    public Integer contarNoLeidas(Long idUsuario);
    List<NotificacionDeUsuario> obtenerListaDeNotificacionesNoLeidas(Usuario usuario);
}
