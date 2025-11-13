package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class ServicioNotificacionDeUsuarioImpl implements ServicioNotificacionDeUsuario{
    private RepositorioNotificacion repositorioNotificacion;

    public ServicioNotificacionDeUsuarioImpl(RepositorioNotificacion repositorioNotificacion) {
        this.repositorioNotificacion = repositorioNotificacion;
    }

    @Override
    public void crearNotificacion(Usuario usuario, String mensaje) {
    NotificacionDeUsuario notificacionDeUsuario = new NotificacionDeUsuario(usuario, mensaje);
    notificacionDeUsuario.setFecha(LocalDate.now());
    repositorioNotificacion.guardar(notificacionDeUsuario);
    }

    @Override
    public List<NotificacionDeUsuario> obtenerListaDeNotificaciones(Usuario usuario) {
        return repositorioNotificacion.obtenerListaDeNotificaciones(usuario);
    }

    @Override
    public void marcarComoLeida(Long id) {
    repositorioNotificacion.marcarComoLeida(id);
    }

    @Override
    public void eliminarNotificacion(Long id){
        NotificacionDeUsuario notificacion = repositorioNotificacion.obtenerNotificacion(id);
        if(notificacion!=null){
            repositorioNotificacion.eliminarNotificacion(notificacion);
        }

    }

    @Override
    public NotificacionDeUsuario obtenerNotificacionPorId(Long id) {
    return repositorioNotificacion.obtenerNotificacion(id);
    }

    @Override
    public String marcarComoLeidaYObtenerUsername(Long idNotificacion) {

        NotificacionDeUsuario notificacion = repositorioNotificacion.obtenerNotificacion(idNotificacion);
        if (notificacion == null)
            return null;

        // Marcar como leída
        notificacion.setLeida(true);
        repositorioNotificacion.actualizar(notificacion);

        // Extraer username del mensaje
        String mensaje = notificacion.getMensaje();
        if (mensaje != null && mensaje.contains("El usuario") && mensaje.contains("te ha enviado")) {
            try {
                return mensaje.split("El usuario")[1].split("te ha enviado")[0].trim();
            } catch (Exception ignored) { }
        }

        return null;
    }
}
