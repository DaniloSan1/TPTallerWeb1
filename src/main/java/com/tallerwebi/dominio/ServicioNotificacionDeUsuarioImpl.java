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
    public void crearNotificacion(Usuario usuario, String mensaje, NotificacionEnum tipoDeNotificacion, Long idPartido) {
            NotificacionDeUsuario notificacionDeUsuario = new NotificacionDeUsuario(usuario, mensaje, tipoDeNotificacion, idPartido);
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

        // Según el tipo de notificación
        NotificacionEnum tipo = notificacion.getTipoDeNotificacion();

        //sepramaos el username del remitente del texto
        // PARA SOLICITUD DE AMISTAD
        if (tipo == NotificacionEnum.SOLICITUD_AMISTAD) {
            // mensaje esperado: "El usuario X te ha enviado una solicitud"
            String mensaje = notificacion.getMensaje();
            try {
                return mensaje.split("El usuario")[1].split("te ha enviado")[0].trim();
            } catch (Exception ignored) {}
        }

        // PARA SOLICITUD ACEPTADA
        if (tipo == NotificacionEnum.SOLICITUD_ACEPTADA) {
            // mensaje esperado: "El usuario X ha aceptado tu solicitud"
            String mensaje = notificacion.getMensaje();
            try {
                return mensaje.split("El usuario")[1].split("ha aceptado")[0].trim();
            } catch (Exception ignored) {}
        }

        //PARA SINVITACION DE PARTIDO
        if(tipo == NotificacionEnum.INVITACION_PARTIDO){
            //mensaje essperado = "El usuario X te ha invitado a ub partido"
            String mensaje = notificacion.getMensaje();
            try{
                return mensaje.split("El usuario")[1].split("te ha invitado")[0].trim();
            } catch (Exception ignored) {}
        }

        return null;
    }

    @Override
    public Integer contarNoLeidas(Long idUsuario) {
        return repositorioNotificacion.contarNoLeidas(idUsuario);
    }

    @Override
    public List<NotificacionDeUsuario> obtenerListaDeNotificacionesNoLeidas(Usuario usuario) {
        return repositorioNotificacion.obtenerListaDeNotificacionesNoLeidas(usuario);
    }

    @Override
    public Long marcarComoLeidaYObtenerIdDePartido(Long idNotificacion) {
        NotificacionDeUsuario n = repositorioNotificacion.obtenerNotificacion(idNotificacion);
        if (n == null) return null;

        n.setLeida(true);
        repositorioNotificacion.actualizar(n);

        if (n.getTipoDeNotificacion() == NotificacionEnum.INVITACION_PARTIDO) {
            return n.getReferenciaIdPartido();
        }

        return null;
    }


}
