package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Amistad;
import com.tallerwebi.dominio.NotificacionDeUsuario;
import com.tallerwebi.dominio.RepositorioNotificacion;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RepositorioNotificacionDeUsuarioImpl implements RepositorioNotificacion {
    private SessionFactory sessionFactory;

    public RepositorioNotificacionDeUsuarioImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(NotificacionDeUsuario notificacionDeUsuario) {
    this.sessionFactory.getCurrentSession().save(notificacionDeUsuario);
    }

    @Override
    public List<NotificacionDeUsuario> obtenerListaDeNotificaciones(Usuario usuario) {
            String hql = "FROM NotificacionDeUsuario n WHERE n.usuario = :usuario ORDER BY n.fecha DESC";
            return sessionFactory.getCurrentSession()
                .createQuery(hql, NotificacionDeUsuario.class)
                .setParameter("usuario",usuario)
                .getResultList();
        }

    @Override
    public void marcarComoLeida(Long id) {
    NotificacionDeUsuario notificacion =  sessionFactory.getCurrentSession().get(NotificacionDeUsuario.class, id);
    notificacion.setLeida(true);
    sessionFactory.getCurrentSession().saveOrUpdate(notificacion);
    }

    @Override
    public void eliminarNotificacion(NotificacionDeUsuario notificacionDeUsuario) {
    sessionFactory.getCurrentSession().delete(notificacionDeUsuario);
    }

    @Override
    public NotificacionDeUsuario obtenerNotificacion(Long id) {
        return sessionFactory.getCurrentSession().get(NotificacionDeUsuario.class, id);
    }

    @Override
    public void actualizar(NotificacionDeUsuario notificacion) {
        sessionFactory.getCurrentSession().update(notificacion);
    }

    @Override
    public Integer contarNoLeidas(Long idUsuario) {
        String hql = "SELECT COUNT(n) FROM NotificacionDeUsuario n " +
            "WHERE n.usuario.id = :idUsuario AND n.leida = false";

        Long resultado = sessionFactory.getCurrentSession()
            .createQuery(hql, Long.class)
            .setParameter("idUsuario", idUsuario)
            .uniqueResult();

        return resultado != null ? resultado.intValue() : 0;
    }


}


