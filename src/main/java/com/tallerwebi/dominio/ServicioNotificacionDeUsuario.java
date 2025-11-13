package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServicioNotificacionDeUsuario {
    public void crearNotificacion(Usuario usuario, String mensaje);
    List<NotificacionDeUsuario> obtenerListaDeNotificaciones(Usuario usuario);
    public void marcarComoLeida(Long id);
    public void eliminarNotificacion(Long id);
    public NotificacionDeUsuario obtenerNotificacionPorId(Long id);

}
